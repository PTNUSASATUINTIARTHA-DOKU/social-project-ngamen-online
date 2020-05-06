package men.doku.donation.service.dto;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import men.doku.donation.domain.Organizer;
import men.doku.donation.domain.enumeration.PaymentChannel;
import men.doku.donation.domain.enumeration.TransactionStatus;

public class DailyReportSuccessDTO {

    @JsonProperty("Transaction ID")
    private Long transactionId;

    @JsonProperty("Invoice Number")
    private String invoiceNumber;

    @JsonProperty("Amount")
    private Long amount;

    @JsonProperty("Payment Channel")
    private PaymentChannel paymentChannel;

    @JsonProperty("Payment Date")
    private String paymentDate;

    @JsonProperty("Approval Code")
    private String approvalCode;

    @JsonProperty("Status")
    private TransactionStatus status;

    @JsonProperty("Donation Name")
    private String donationName;

    @JsonProperty("Donation Bank Account")
    private String donationBankAccount;

    @JsonIgnore
    private Organizer organizer;

    @JsonProperty("Organizer Bank Account")
    private String organizerBankAccount;

    @JsonProperty("MDR %")
    private Float organizerMdr;
    
    @JsonProperty("MDR Rp.")
    private Long mdrAmount;

    @JsonProperty("Donation %")
    private Float organizerSharing;

    @JsonProperty("Donation Rp.")
    private Long settlementToSharingDonation;
 
    @JsonProperty("Settlement")
    private Long settlementToOrganizer;

    public DailyReportSuccessDTO(Long transactionId, String invoiceNumber, Long amount,
            PaymentChannel paymentChannel, Instant paymentDate, String approvalCode, TransactionStatus status,
            String donationName, String donationBankAccountNumber, String donationBankAccountName,
            String donationBankName, Organizer organizer) {
        this.transactionId = transactionId;
        this.invoiceNumber = invoiceNumber;
        this.amount = amount;
        this.paymentChannel = paymentChannel;
        this.paymentDate = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withZone(ZoneId.of("Asia/Jakarta")).format(paymentDate);
        this.approvalCode = approvalCode;
        this.status = status;
        this.donationName = donationName;
        this.donationBankAccount = donationBankName + " " + donationBankAccountNumber + " a/n " + donationBankAccountName;
        this.organizer = organizer;
        this.organizerMdr = organizer.getMdr();
        this.organizerSharing = organizer.getSharing();
        this.organizerBankAccount = organizer.getBankName() + " " + organizer.getBankAccountNumber() + " a/n " + organizer.getBankAccountName();
        this.mdrAmount = Double.valueOf(Math.floor(this.amount * this.organizerMdr / 100)).longValue();
        this.settlementToSharingDonation = Double.valueOf(Math.floor((this.amount - this.mdrAmount) * this.organizerSharing / 100)).longValue();
        this.settlementToOrganizer = this.amount - this.mdrAmount - this.settlementToSharingDonation;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
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

    public PaymentChannel getPaymentChannel() {
        return paymentChannel;
    }

    public void setPaymentChannel(PaymentChannel paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public String getDonationName() {
        return donationName;
    }

    public void setDonationName(String donationName) {
        this.donationName = donationName;
    }

    public String getDonationBankAccount() {
        return donationBankAccount;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public String getOrganizerBankAccount() {
        return organizerBankAccount;
    }

    public Float getOrganizerMdr() {
        return organizerMdr;
    }

    public Float getOrganizerSharing() {
        return organizerSharing;
    }

    public Long getMdrAmount() {
        return mdrAmount;
    }

    public Long getSettlementToOrganizer() {
        return settlementToOrganizer;
    }

    public Long getSettlementToSharingDonation() {
        return settlementToSharingDonation;
    }

    @Override
    public String toString() {
        return "DailyReportSuccessDTO [amount=" + amount + ", approvalCode=" + approvalCode 
                + ", donationBankAccount=" + donationBankAccount + ", donationName=" + donationName + ", invoiceNumber="
                + invoiceNumber + ", mdrAmount=" + mdrAmount + ", organizer=" + organizer + ", organizerBankAccount="
                + organizerBankAccount + ", organizerMdr=" + organizerMdr + ", organizerSharing=" + organizerSharing
                + ", paymentChannel=" + paymentChannel + ", paymentDate=" + paymentDate + ", settlementToOrganizer="
                + settlementToOrganizer + ", settlementToSharingDonation=" + settlementToSharingDonation + ", status="
                + status + ", transactionId=" + transactionId + "]";
    }
}
