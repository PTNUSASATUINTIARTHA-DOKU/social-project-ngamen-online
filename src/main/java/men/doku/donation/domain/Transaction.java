package men.doku.donation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import men.doku.donation.domain.enumeration.TransactionStatus;

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
    private Long id;

    @NotNull
    @Size(max = 30)
    @Column(name = "invoice_number", length = 30, nullable = false, unique = true)
    private String invoiceNumber;

    @Size(max = 128)
    @Column(name = "session_id", length = 128)
    private String sessionId;

    @Size(max = 1024)
    @Column(name = "basket", length = 1024)
    private String basket;

    @Size(max = 15)
    @Column(name = "ovo_id_masked", length = 15)
    private String ovoIdMasked;

    @Size(max = 1000)
    @Column(name = "device_information", length = 1000)
    private String deviceInformation;

    @Size(max = 100)
    @Column(name = "last_updated_by", length = 100)
    private String lastUpdatedBy;

    @Column(name = "last_updated_at")
    private Instant lastUpdatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TransactionStatus status;

    @ManyToOne
    @JsonIgnoreProperties("transactions")
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
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            ", lastUpdatedAt='" + getLastUpdatedAt() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
