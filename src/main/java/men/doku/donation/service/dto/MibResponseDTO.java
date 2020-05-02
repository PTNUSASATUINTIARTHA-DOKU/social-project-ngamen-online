package men.doku.donation.service.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MIBPaymentResponse")
public class MibResponseDTO {

    private Integer mallId;
    private String trxCode;
    private String auth1;
    private String invoiceNumber;
    private Long amount;
    private String currency;
    private String sessionId;
    private Long paymentDate;
    private String result;
    private String responseCode;
    private String message;
    private String paymentSysTrace;
    private String approvalCode;
    private String paymentHostRefNumber;

    /**
     * Constructor
     */
    public MibResponseDTO() {
    }

    /**
     * 
     * Constructor with all attribute
     * 
     * @param mallId merchant identifier
     * @param trxCode MIB system identifier
     * @param auth1 masked OVO ID 
     * @param invoiceNumber Merchant transaction identifier
     * @param amount payment amount
     * @param currency IDR
     * @param sessionId for merchant use
     * @param paymentDate payment date
     * @param result SUCCESS / FAILED
     * @param responseCode response code for payment result
     * @param message response message for payment result
     * @param paymentSysTrace MIB System identifier that being sent to OVO
     * @param approvalCode payment success identifier from OVO
     * @param paymentHostRefNumber payment identifier from OVO
     */
    public MibResponseDTO(Integer mallId, String trxCode, String auth1, String invoiceNumber, Long amount,
            String currency, String sessionId, Long paymentDate, String result, String responseCode, String message,
            String paymentSysTrace, String approvalCode, String paymentHostRefNumber) {
        this.mallId = mallId;
        this.trxCode = trxCode;
        this.auth1 = auth1;
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
        this.currency = currency;
        this.sessionId = sessionId;
        this.paymentDate = paymentDate;
        this.result = result;
        this.responseCode = responseCode;
        this.message = message;
        this.paymentSysTrace = paymentSysTrace;
        this.approvalCode = approvalCode;
        this.paymentHostRefNumber = paymentHostRefNumber;
    }

    public Integer getMallId() {
        return mallId;
    }

    public void setMallId(Integer mallId) {
        this.mallId = mallId;
    }

    public String getTrxCode() {
        return trxCode;
    }

    public void setTrxCode(String trxCode) {
        this.trxCode = trxCode;
    }

    public String getAuth1() {
        return auth1;
    }

    public void setAuth1(String auth1) {
        this.auth1 = auth1;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Long paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPaymentSysTrace() {
        return paymentSysTrace;
    }

    public void setPaymentSysTrace(String paymentSysTrace) {
        this.paymentSysTrace = paymentSysTrace;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public String getPaymentHostRefNumber() {
        return paymentHostRefNumber;
    }

    public void setPaymentHostRefNumber(String paymentHostRefNumber) {
        this.paymentHostRefNumber = paymentHostRefNumber;
    }

    @Override
    public String toString() {
        return "MibResponseDTO [amount=" + amount + ", approvalCode=" + approvalCode + ", auth1=" + auth1
                + ", currency=" + currency + ", invoiceNumber=" + invoiceNumber + ", mallId=" + mallId + ", message="
                + message + ", paymentDate=" + paymentDate + ", paymentHostRefNumber=" + paymentHostRefNumber
                + ", paymentSysTrace=" + paymentSysTrace + ", responseCode=" + responseCode + ", result=" + result
                + ", sessionId=" + sessionId + ", trxCode=" + trxCode + "]";
    }
}
