package men.doku.donation.service.mapper;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import men.doku.donation.config.Constants;
import men.doku.donation.domain.Organizer;
import men.doku.donation.domain.Transaction;
import men.doku.donation.domain.enumeration.TransactionStatus;
import men.doku.donation.service.dto.MibRequestDTO;
import men.doku.donation.service.dto.MibResponseDTO;

@Service
public class MibMapper {

    public MibRequestDTO toMibRequestDTO(Transaction transaction, Organizer organizer) {
        MibRequestDTO mibRequestDTO = new MibRequestDTO();
        mibRequestDTO.setMALLID(String.valueOf(organizer.getMallId()));
        mibRequestDTO.setSERVICEID(String.valueOf(organizer.getServiceId()));
        mibRequestDTO.setACQUIRERID(String.valueOf(organizer.getAcquirerId()));
        mibRequestDTO.setINVOICENUMBER(transaction.getInvoiceNumber());
        mibRequestDTO.setCURRENCY(Constants.DEFAULT_CURRENCY);
        mibRequestDTO.setAMOUNT(String.valueOf(transaction.getAmount()));
        mibRequestDTO.setSESSIONID(transaction.getSessionId());
        mibRequestDTO.setAUTH1(transaction.getMobile());
        mibRequestDTO.setBASKET(transaction.getBasket());
        String raw = mibRequestDTO.getAMOUNT() + mibRequestDTO.getMALLID()
                 + mibRequestDTO.getINVOICENUMBER() + organizer.getSharedKey() + mibRequestDTO.getCURRENCY();
        MessageDigest md = null;
        byte[] words = raw.getBytes(StandardCharsets.UTF_8);
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(words);
            words = md.digest();
            mibRequestDTO.setWORDS(String.format("%040x", new BigInteger(1, words)));
        } catch (NoSuchAlgorithmException e) {
            mibRequestDTO.setWORDS("Failed to hash WORDS");
        }
        return mibRequestDTO;
    }

    public MultiValueMap<String, String> mibRequestToMultiValueMap(MibRequestDTO mibRequestDTO) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("MALLID", mibRequestDTO.getMALLID());
        body.add("SERVICEID", mibRequestDTO.getSERVICEID());
        body.add("ACQUIRERID", mibRequestDTO.getACQUIRERID());
        body.add("INVOICENUMBER", mibRequestDTO.getINVOICENUMBER());
        body.add("CURRENCY", mibRequestDTO.getCURRENCY());
        body.add("AMOUNT", mibRequestDTO.getAMOUNT());
        body.add("SESSIONID", mibRequestDTO.getSESSIONID());
        body.add("AUTH1", mibRequestDTO.getAUTH1());
        body.add("BASKET", mibRequestDTO.getBASKET());
        body.add("WORDS", mibRequestDTO.getWORDS());        
        return body;
    }

    public MibResponseDTO mibRequestToMibResponse(MibRequestDTO mibRequestDTO) {
        MibResponseDTO mibResponseDTO = new MibResponseDTO();
        mibResponseDTO.setMallId(Integer.valueOf(mibRequestDTO.getMALLID()));
        mibResponseDTO.setTrxCode(UUID.randomUUID().toString().replaceAll("-", ""));
        mibResponseDTO.setAuth1(mibRequestDTO.getAUTH1());
        mibResponseDTO.setInvoiceNumber(mibRequestDTO.getINVOICENUMBER());
        mibResponseDTO.setAmount(mibRequestDTO.getAMOUNT());
        mibResponseDTO.setCurrency(mibRequestDTO.getCURRENCY());
        mibResponseDTO.setSessionId(mibRequestDTO.getSESSIONID());
        mibResponseDTO.setPaymentDate(Long.valueOf(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.of("UTC+07")).format(Instant.now())));
        return mibResponseDTO;
    }

    public Transaction mibResponseToTransaction(MibResponseDTO mibResponseDTO, Transaction transaction) {
        transaction.setMallId(mibResponseDTO.getMallId());
        transaction.setTrxCode(mibResponseDTO.getTrxCode());
        LocalDateTime dateTime = LocalDateTime.parse(String.valueOf(mibResponseDTO.getPaymentDate()),
                DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        Instant instant = dateTime.atZone(ZoneId.of("Asia/Jakarta")).toInstant();
        transaction.setPaymentDate(instant);
        transaction.setResponseCode(mibResponseDTO.getResponseCode());
        transaction.setMessage(mibResponseDTO.getMessage());
        transaction.setPaymentSystrace(mibResponseDTO.getPaymentSystrace());
        transaction.setApprovalCode(mibResponseDTO.getApprovalCode());
        transaction.setPaymentHostRefNumber(mibResponseDTO.getPaymentHostRefNumber());
        transaction.setStatus(TransactionStatus.valueOf(mibResponseDTO.getResult()));
        transaction.setOvoIdMasked(mibResponseDTO.getAuth1());
        return transaction;
    }
}
