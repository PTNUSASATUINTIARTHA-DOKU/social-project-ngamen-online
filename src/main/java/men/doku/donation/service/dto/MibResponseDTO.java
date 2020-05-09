package men.doku.donation.service.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "MIBPaymentResponse")
public class MibResponseDTO {

    private Integer mallId;
    private Integer chainMallId;
    private String trxCode;
    private String auth1;
    private String invoiceNumber;
    private String amount;
    private String currency;
    private String sessionId;
    private Long paymentDate;
    private String result;
    private String responseCode;
    private String message;
    private String paymentSystrace;
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
     * @param chainMallId merchant chain identifier
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
     * @param paymentSystrace MIB System identifier that being sent to OVO
     * @param approvalCode payment success identifier from OVO
     * @param paymentHostRefNumber payment identifier from OVO
     */
    public MibResponseDTO(Integer mallId, Integer chainMallId, String trxCode, String auth1, String invoiceNumber, String amount,
            String currency, String sessionId, Long paymentDate, String result, String responseCode, String message,
            String paymentSystrace, String approvalCode, String paymentHostRefNumber) {
        this.mallId = mallId;
        this.chainMallId = chainMallId;
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
        this.paymentSystrace = paymentSystrace;
        this.approvalCode = approvalCode;
        this.paymentHostRefNumber = paymentHostRefNumber;
    }

    public Integer getMallId() {
        return mallId;
    }

    public void setMallId(Integer mallId) {
        this.mallId = mallId;
    }

    public Integer getChainMallId() {
        return chainMallId;
    }

    public void setChainMallId(Integer chainMallId) {
        this.chainMallId = chainMallId;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
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

    public String getPaymentSystrace() {
        return paymentSystrace;
    }

    public void setPaymentSystrace(String paymentSystrace) {
        this.paymentSystrace = paymentSystrace;
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
        return "MibResponseDTO [amount=" + amount + ", approvalCode=" + approvalCode + ", auth1=" + auth1 + ", chainMallId=" + chainMallId
                + ", currency=" + currency + ", invoiceNumber=" + invoiceNumber + ", mallId=" + mallId + ", message="
                + message + ", paymentDate=" + paymentDate + ", paymentHostRefNumber=" + paymentHostRefNumber
                + ", paymentSysTrace=" + paymentSystrace + ", responseCode=" + responseCode + ", result=" + result
                + ", sessionId=" + sessionId + ", trxCode=" + trxCode + "]";
    }
}
