package men.doku.donation.service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import men.doku.donation.config.ApplicationProperties;
import men.doku.donation.domain.Organizer;
import men.doku.donation.domain.Transaction;
import men.doku.donation.service.dto.DailyReportSuccessDTO;

@Service
public class DailyReportService {

    private final Logger log = LoggerFactory.getLogger(DailyReportService.class);

    private final TransactionService transactionService;
    private final MailService mailService;
    private final ApplicationProperties applicationProperties;

    public DailyReportService(TransactionService transactionService, MailService mailService, ApplicationProperties applicationProperties) {
        this.transactionService = transactionService;
        this.mailService = mailService;
        this.applicationProperties = applicationProperties;
    }


    @Scheduled(cron = "0 0 5 * * *")
    public void generate() {
        log.info("Daily Report Service running");
        generateEmail(1);
    }

    public void generateEmail(Integer daysBefore) {
        Map<Organizer, List<DailyReportSuccessDTO>> data = generateData(daysBefore);
        for (Map.Entry<Organizer, List<DailyReportSuccessDTO>> entry: data.entrySet()) {
            Organizer organizer = entry.getKey();
            String filePath = generateFile(organizer.getName(), data.get(organizer), daysBefore);
            String fileName =  filePath.substring(filePath.indexOf(applicationProperties.getReport().getBasename()));
            mailService.sendEmail(organizer.getEmail(), applicationProperties.getName() + " - " + fileName.replaceAll("_", " "), "Dear all, <br/><br/>Terlampir laporan harian.<br/><br/>Terima kasih."
                    , true, true, fileName, filePath);
        }
    }

    public Map<Organizer, List<DailyReportSuccessDTO>> generateData(Integer daysBefore) {
        List<Transaction> transactions = this.transactionService.findAllSuccessByPaymentDate(
                Instant.now().minus(daysBefore, ChronoUnit.DAYS).atZone(ZoneId.of("Asia/Jakarta")).toLocalDate());
        Map<Organizer, List<DailyReportSuccessDTO>> dailyReports = new HashMap<>();
        transactions.forEach(transaction -> {
            Organizer organizer = transaction.getDonation().getOrganizer();
            List<DailyReportSuccessDTO> dailyReport = dailyReports.get(organizer);
            if (dailyReport == null) dailyReport = new ArrayList<DailyReportSuccessDTO>();
            dailyReport.add(fromTransactionToDTO(transaction));
            dailyReports.put(organizer, dailyReport);
        });
        return dailyReports;
    }

    public String generateFile(String organizerName, List<DailyReportSuccessDTO> data, Integer daysBefore) {
        CsvMapper mapper = new CsvMapper();
        mapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        CsvSchema schema = mapper.schemaFor(DailyReportSuccessDTO.class);
        schema = schema.withColumnSeparator(',');
        schema = schema.withHeader();

        ObjectWriter writer = mapper.writer(schema);
        String fileName = applicationProperties.getReport().getFolder() + applicationProperties.getReport().getBasename()
             + organizerName.replace(' ', '_') + "_"  
             + Instant.now().minus(daysBefore, ChronoUnit.DAYS).atZone(ZoneId.of("Asia/Jakarta")).toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) 
             + ".csv";
        File file = new File(fileName);
        file.getParentFile().mkdirs();
        try {
            writer.writeValue(file, data);
        } catch (JsonGenerationException e) {
            log.error("Failed to generate CSV from JSON {} ", e.getMessage());
            e.printStackTrace();
        } catch (JsonMappingException e) {
            log.error("Failed to mapping JSON {} ", e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            log.error("Failed to write to CSV {} ", e.getMessage());
            e.printStackTrace();
        }    
        return fileName;
    } 

    private DailyReportSuccessDTO fromTransactionToDTO(Transaction transaction) {
        return new DailyReportSuccessDTO(
            transaction.getId(), 
            transaction.getInvoiceNumber(), 
            transaction.getAmount(), 
            transaction.getPaymentChannel(), 
            transaction.getPaymentDate(), 
            transaction.getApprovalCode(), 
            transaction.getStatus(), 
            transaction.getDonation().getName(), 
            transaction.getDonation().getBankAccountNumber(),
            transaction.getDonation().getBankAccountName(), 
            transaction.getDonation().getBankName(), 
            transaction.getDonation().getOrganizer());
    }
}