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
import java.util.HashSet;
import java.util.Set;

import men.doku.donation.config.Constants;
import men.doku.donation.domain.enumeration.IsActiveStatus;
import men.doku.donation.security.AuthoritiesConstants;

/**
 * Donation entity.\n@author RT.
 */
@ApiModel(description = "Donation entity.\n@author RT.")
@Entity
@Table(name = "donation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Donation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false, unique = true)
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String name;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String description;

    @Pattern(regexp = Constants.URL_REGEX)
    @Size(max = 100)
    @Column(name = "url", length = 100)
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String url;

    @Pattern(regexp = Constants.URL_REGEX)
    @Size(max = 100)
    @Column(name = "image_url", length = 100)
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String imageUrl;

    @Size(max = 100)
    @Column(name = "payment_slug", length = 100)
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String paymentSlug;

    @Size(max = 15)
    @Column(name = "bank_account_number", length = 15)
    @JsonView(AuthoritiesConstants.User.class)
    private String bankAccountNumber;

    @Size(max = 100)
    @Column(name = "bank_account_name", length = 100)
    @JsonView(AuthoritiesConstants.User.class)
    private String bankAccountName;

    @Size(max = 100)
    @Column(name = "bank_name", length = 100)
    @JsonView(AuthoritiesConstants.User.class)
    private String bankName;

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
    private IsActiveStatus status;

    @OneToMany(mappedBy = "donation")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Transaction> transactions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("donations")
    @NotNull
    private Organizer organizer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Donation name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Donation description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public Donation url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Donation imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPaymentSlug() {
        return paymentSlug;
    }

    public Donation paymentSlug(String paymentSlug) {
        this.paymentSlug = paymentSlug;
        return this;
    }

    public void setPaymentSlug(String paymentSlug) {
        this.paymentSlug = paymentSlug;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public Donation bankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
        return this;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public Donation bankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
        return this;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankName() {
        return bankName;
    }

    public Donation bankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public Donation lastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Instant getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public Donation lastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
        return this;
    }

    public void setLastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public IsActiveStatus getStatus() {
        return status;
    }

    public Donation status(IsActiveStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(IsActiveStatus status) {
        this.status = status;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public Donation transactions(Set<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public Donation addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        transaction.setDonation(this);
        return this;
    }

    public Donation removeTransaction(Transaction transaction) {
        this.transactions.remove(transaction);
        transaction.setDonation(null);
        return this;
    }

    public void setTransactions(Set<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Organizer getOrganizer() {
        return organizer;
    }

    public Donation organizer(Organizer organizer) {
        this.organizer = organizer;
        return this;
    }

    public void setOrganizer(Organizer organizer) {
        this.organizer = organizer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Donation)) {
            return false;
        }
        return id != null && id.equals(((Donation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Donation{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", url='" + getUrl() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", paymentSlug='" + getPaymentSlug() + "'" +
            ", bankAccountNumber='" + getBankAccountNumber() + "'" +
            ", bankAccountName='" + getBankAccountName() + "'" +
            ", bankName='" + getBankName() + "'" +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            ", lastUpdatedAt='" + getLastUpdatedAt() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
