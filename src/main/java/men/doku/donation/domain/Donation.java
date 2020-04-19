package men.doku.donation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import men.doku.donation.domain.enumeration.IsActiveStatus;

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
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false, unique = true)
    private String name;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @Size(max = 100)
    @Column(name = "url", length = 100)
    private String url;

    @Size(max = 100)
    @Column(name = "image_url", length = 100)
    private String imageUrl;

    @NotNull
    @Size(max = 100)
    @Column(name = "payment_url", length = 100, nullable = false, unique = true)
    private String paymentUrl;

    @NotNull
    @Size(max = 100)
    @Column(name = "last_updated_by", length = 100, nullable = false)
    private String lastUpdatedBy;

    @NotNull
    @Column(name = "last_updated_at", nullable = false)
    private Instant lastUpdatedAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private IsActiveStatus status;

    @OneToMany(mappedBy = "donation")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Transaction> transactions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("donations")
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

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public Donation paymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
        return this;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
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
            ", paymentUrl='" + getPaymentUrl() + "'" +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            ", lastUpdatedAt='" + getLastUpdatedAt() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
