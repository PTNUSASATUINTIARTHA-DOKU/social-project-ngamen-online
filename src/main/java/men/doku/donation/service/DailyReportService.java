package men.doku.donation.service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
import org.springframework.stereotype.Service;

import men.doku.donation.domain.Transaction;
import men.doku.donation.service.dto.DailyReportSuccessDTO;

@Service
public class DailyReportService {

    private final Logger log = LoggerFactory.getLogger(DailyReportService.class);

    private final TransactionService transactionService;

    public DailyReportService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public void generate() {
        Map<String, List<DailyReportSuccessDTO>> data = generateData();
        List<File> files = new ArrayList<>();
        for (Map.Entry<String, List<DailyReportSuccessDTO>> entry: data.entrySet()) {
            String[] key = entry.getKey().split("|");
            String organizerName = key[0].replace(' ', '_');
            File file = generateFile(organizerName, data.get(entry.getKey()));
            files.add(file);
        }
    }

    public Map<String, List<DailyReportSuccessDTO>> generateData() {
        List<Transaction> transactions = this.transactionService.findAllSuccessByPaymentDate(Instant.now().atZone(ZoneId.of("Asia/Jakarta")).toLocalDate());
        Map<String, List<DailyReportSuccessDTO>> dailyReports = new HashMap<>();
        transactions.forEach(transaction -> {
            String key = transaction.getDonation().getOrganizer().getName() + "|" + transaction.getDonation().getOrganizer().getEmail();
            List<DailyReportSuccessDTO> dailyReport = dailyReports.get(key);
            if (dailyReport == null) dailyReport = new ArrayList<DailyReportSuccessDTO>();
            dailyReport.add(fromTransactionToDTO(transaction));
            dailyReports.put(key, dailyReport);
        });
        return dailyReports;
    }

    public File generateFile(String organizerName, List<DailyReportSuccessDTO> data) {
        CsvMapper mapper = new CsvMapper();
        mapper.disable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        CsvSchema schema = mapper.schemaFor(DailyReportSuccessDTO.class);
        schema = schema.withColumnSeparator(',');
        schema = schema.withHeader();

        ObjectWriter writer = mapper.writer(schema);
        String fileName = "donation_" + organizerName.replace(' ', '_') + "_"  + Instant.now().atZone(ZoneId.of("Asia/Jakarta")).toLocalDate().format(DateTimeFormatter.ofPattern("yyyy_MM_dd")) + ".csv";
        File file = new File(fileName);
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
        return file;
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