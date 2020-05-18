package men.doku.donation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import men.doku.donation.domain.enumeration.PaymentChannel;

import men.doku.donation.domain.enumeration.TransactionStatus;
import men.doku.donation.security.AuthoritiesConstants;

/**
 * Transaction entity.\n@author RT.
 */
@ApiModel(description = "Transaction entity.\n@author RT.")
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private Long id;

    @NotNull
    @Size(max = 64)
    @Column(name = "invoice_number", length = 64, nullable = false, unique = true)
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String invoiceNumber;

    @Size(max = 128)
    @Column(name = "session_id", length = 128)
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String sessionId;

    @Size(max = 1024)
    @Column(name = "basket", length = 1024)
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String basket;

    @Size(max = 15)
    @Column(name = "ovo_id_masked", length = 15)
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String ovoIdMasked;

    @Size(max = 1000)
    @Column(name = "device_information", length = 1000)
    @JsonView(AuthoritiesConstants.User.class)
    private String deviceInformation;

    @Size(max = 30)
    @Column(name = "name", length = 30)
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String name;

    @Size(max = 15)
    @Column(name = "mobile", length = 15)
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String mobile;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String email;

    @Min(value = 10000L)
    @Column(name = "amount")
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_channel")
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private PaymentChannel paymentChannel;

    @Column(name = "mall_id")
    @JsonView(AuthoritiesConstants.User.class)
    private Integer mallId;

    @Column(name = "trx_code")
    @JsonView(AuthoritiesConstants.User.class)
    private String trxCode;

    @Column(name = "payment_date")
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private Instant paymentDate;

    @Column(name = "response_code")
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String responseCode;

    @Column(name = "message")
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String message;

    @Column(name = "payment_systrace")
    @JsonView(AuthoritiesConstants.User.class)
    private String paymentSystrace;

    @Column(name = "approval_code")
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String approvalCode;

    @Column(name = "payment_host_ref_number")
    @JsonView(AuthoritiesConstants.User.class)
    private String paymentHostRefNumber;

    @Size(max = 1000)
    @Column(name = "captcha_token", length = 1000)
    @JsonView(AuthoritiesConstants.User.class)
    private String captchaToken;

    @Column(name = "captcha_score")
    @JsonView(AuthoritiesConstants.User.class)
    private Float captchaScore;

    @Size(max = 100)
    @Column(name = "last_updated_by", length = 100)
    @JsonView(AuthoritiesConstants.User.class)
    private String lastUpdatedBy;

    @Column(name = "last_updated_at")
    @JsonView(AuthoritiesConstants.User.class)
    private Instant lastUpdatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private TransactionStatus status;

    @ManyToOne
    @JsonIgnoreProperties("transactions")
    @NotNull
    private Donation donation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public Transaction invoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getSessionId() {
        return sessionId;
    }

    public Transaction sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getBasket() {
        return basket;
    }

    public Transaction basket(String basket) {
        this.basket = basket;
        return this;
    }

    public void setBasket(String basket) {
        this.basket = basket;
    }

    public String getOvoIdMasked() {
        return ovoIdMasked;
    }

    public Transaction ovoIdMasked(String ovoIdMasked) {
        this.ovoIdMasked = ovoIdMasked;
        return this;
    }

    public void setOvoIdMasked(String ovoIdMasked) {
        this.ovoIdMasked = ovoIdMasked;
    }

    public String getDeviceInformation() {
        return deviceInformation;
    }

    public Transaction deviceInformation(String deviceInformation) {
        this.deviceInformation = deviceInformation;
        return this;
    }

    public void setDeviceInformation(String deviceInformation) {
        this.deviceInformation = deviceInformation;
    }

    public String getName() {
        return name;
    }

    public Transaction name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public Transaction mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public Transaction email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getAmount() {
        return amount;
    }

    public Transaction amount(Long amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public PaymentChannel getPaymentChannel() {
        return paymentChannel;
    }

    public Transaction paymentChannel(PaymentChannel paymentChannel) {
        this.paymentChannel = paymentChannel;
        return this;
    }

    public void setPaymentChannel(PaymentChannel paymentChannel) {
        this.paymentChannel = paymentChannel;
    }

    public Integer getMallId() {
        return mallId;
    }

    public Transaction mallId(Integer mallId) {
        this.mallId = mallId;
        return this;
    }

    public void setMallId(Integer mallId) {
        this.mallId = mallId;
    }

    public String getTrxCode() {
        return trxCode;
    }

    public Transaction trxCode(String trxCode) {
        this.trxCode = trxCode;
        return this;
    }

    public void setTrxCode(String trxCode) {
        this.trxCode = trxCode;
    }

    public Instant getPaymentDate() {
        return paymentDate;
    }

    public Transaction paymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
        return this;
    }

    public void setPaymentDate(Instant paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public Transaction responseCode(String responseCode) {
        this.responseCode = responseCode;
        return this;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public Transaction message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPaymentSystrace() {
        return paymentSystrace;
    }

    public Transaction paymentSystrace(String paymentSystrace) {
        this.paymentSystrace = paymentSystrace;
        return this;
    }

    public void setPaymentSystrace(String paymentSystrace) {
        this.paymentSystrace = paymentSystrace;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public Transaction approvalCode(String approvalCode) {
        this.approvalCode = approvalCode;
        return this;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public String getPaymentHostRefNumber() {
        return paymentHostRefNumber;
    }

    public Transaction paymentHostRefNumber(String paymentHostRefNumber) {
        this.paymentHostRefNumber = paymentHostRefNumber;
        return this;
    }

    public void setPaymentHostRefNumber(String paymentHostRefNumber) {
        this.paymentHostRefNumber = paymentHostRefNumber;
    }

    public String getCaptchaToken() {
        return captchaToken;
    }

    public Transaction captchaToken(String captchaToken) {
        this.captchaToken = captchaToken;
        return this;
    }

    public void setCaptchaToken(String captchaToken) {
        this.captchaToken = captchaToken;
    }

    public Float getCaptchaScore() {
        return captchaScore;
    }

    public Transaction captchaScore(Float captchaScore) {
        this.captchaScore = captchaScore;
        return this;
    }

    public void setCaptchaScore(Float captchaScore) {
        this.captchaScore = captchaScore;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public Transaction lastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Instant getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public Transaction lastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
        return this;
    }

    public void setLastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public Transaction status(TransactionStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    public Donation getDonation() {
        return donation;
    }

    public Transaction donation(Donation donation) {
        this.donation = donation;
        return this;
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return id != null && id.equals(((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", sessionId='" + getSessionId() + "'" +
            ", basket='" + getBasket() + "'" +
            ", ovoIdMasked='" + getOvoIdMasked() + "'" +
            ", deviceInformation='" + getDeviceInformation() + "'" +
            ", name='" + getName() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", email='" + getEmail() + "'" +
            ", amount=" + getAmount() +
            ", paymentChannel='" + getPaymentChannel() + "'" +
            ", mallId=" + getMallId() +
            ", trxCode='" + getTrxCode() + "'" +
            ", paymentDate='" + getPaymentDate() + "'" +
            ", responseCode='" + getResponseCode() + "'" +
            ", message='" + getMessage() + "'" +
            ", paymentSystrace='" + getPaymentSystrace() + "'" +
            ", approvalCode='" + getApprovalCode() + "'" +
            ", paymentHostRefNumber='" + getPaymentHostRefNumber() + "'" +
            ", captchaToken='" + getCaptchaToken() + "'" +
            ", captchaScore=" + getCaptchaScore() +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            ", lastUpdatedAt='" + getLastUpdatedAt() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
