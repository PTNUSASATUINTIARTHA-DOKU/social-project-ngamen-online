package men.doku.donation.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import men.doku.donation.config.Constants;
import men.doku.donation.domain.enumeration.IsActiveStatus;
import men.doku.donation.security.AuthoritiesConstants;

/**
 * Organizer entity.\n@author RT.
 */
@ApiModel(description = "Organizer entity.\n@author RT.")
@Entity
@Table(name = "organizer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Organizer implements Serializable {

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

    @Pattern(regexp = Constants.URL_REGEX)
    @Size(max = 100)
    @Column(name = "url", length = 100)
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String url;

    @Pattern(regexp = Constants.EMAIL_REGEX)
    @NotNull
    @Size(max = 100)
    @Column(name = "email", length = 100, nullable = false)
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private String email;

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

    @DecimalMax(value = "100")
    @Column(name = "mdr")
    @JsonView(AuthoritiesConstants.User.class)
    private Float mdr;

    @DecimalMax(value = "100")
    @Column(name = "sharing")
    @JsonView(AuthoritiesConstants.User.class)
    private Float sharing;

    @Size(max = 100)
    @Column(name = "last_updated_by", length = 100)
    @JsonView(AuthoritiesConstants.User.class)
    private String lastUpdatedBy;

    @Column(name = "last_updated_at")
    @JsonView(AuthoritiesConstants.User.class)
    private Instant lastUpdatedAt;

    @Column(name = "mall_id")
    @JsonView(AuthoritiesConstants.User.class)
    private Integer mallId;

    @Column(name = "shared_key")
    @JsonView(AuthoritiesConstants.User.class)
    private String sharedKey;

    @Column(name = "service_id")
    @JsonView(AuthoritiesConstants.User.class)
    private Integer serviceId;

    @Column(name = "acquirer_id")
    @JsonView(AuthoritiesConstants.User.class)
    private Integer acquirerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @JsonView(AuthoritiesConstants.Anonymous.class)
    private IsActiveStatus status;

    @OneToMany(mappedBy = "organizer")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Donation> donations = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "organizer_user",
               joinColumns = @JoinColumn(name = "organizer_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @JsonView(AuthoritiesConstants.User.class)
    private Set<User> users = new HashSet<>();

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

    public Organizer name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public Organizer url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public Organizer email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public Organizer bankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
        return this;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankAccountName() {
        return bankAccountName;
    }

    public Organizer bankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
        return this;
    }

    public void setBankAccountName(String bankAccountName) {
        this.bankAccountName = bankAccountName;
    }

    public String getBankName() {
        return bankName;
    }

    public Organizer bankName(String bankName) {
        this.bankName = bankName;
        return this;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Float getMdr() {
        return mdr;
    }

    public Organizer mdr(Float mdr) {
        this.mdr = mdr;
        return this;
    }

    public void setMdr(Float mdr) {
        this.mdr = mdr;
    }

    public Float getSharing() {
        return sharing;
    }

    public Organizer sharing(Float sharing) {
        this.sharing = sharing;
        return this;
    }

    public void setSharing(Float sharing) {
        this.sharing = sharing;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public Organizer lastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
        return this;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Instant getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public Organizer lastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
        return this;
    }

    public void setLastUpdatedAt(Instant lastUpdatedAt) {
        this.lastUpdatedAt = lastUpdatedAt;
    }

    public Integer getMallId() {
        return mallId;
    }

    public Organizer mallId(Integer mallId) {
        this.mallId = mallId;
        return this;
    }

    public void setMallId(Integer mallId) {
        this.mallId = mallId;
    }

    public String getSharedKey() {
        return sharedKey;
    }

    public Organizer sharedKey(String sharedKey) {
        this.sharedKey = sharedKey;
        return this;
    }

    public void setSharedKey(String sharedKey) {
        this.sharedKey = sharedKey;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public Organizer serviceId(Integer serviceId) {
        this.serviceId = serviceId;
        return this;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getAcquirerId() {
        return acquirerId;
    }

    public Organizer acquirerId(Integer acquirerId) {
        this.acquirerId = acquirerId;
        return this;
    }

    public void setAcquirerId(Integer acquirerId) {
        this.acquirerId = acquirerId;
    }

    public IsActiveStatus getStatus() {
        return status;
    }

    public Organizer status(IsActiveStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(IsActiveStatus status) {
        this.status = status;
    }

    public Set<Donation> getDonations() {
        return donations;
    }

    public Organizer donations(Set<Donation> donations) {
        this.donations = donations;
        return this;
    }

    public Organizer addDonation(Donation donation) {
        this.donations.add(donation);
        donation.setOrganizer(this);
        return this;
    }

    public Organizer removeDonation(Donation donation) {
        this.donations.remove(donation);
        donation.setOrganizer(null);
        return this;
    }

    public void setDonations(Set<Donation> donations) {
        this.donations = donations;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Organizer users(Set<User> users) {
        this.users = users;
        return this;
    }

    public Organizer addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Organizer removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organizer)) {
            return false;
        }
        return id != null && id.equals(((Organizer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Organizer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", url='" + getUrl() + "'" +
            ", email='" + getEmail() + "'" +
            ", bankAccountNumber='" + getBankAccountNumber() + "'" +
            ", bankAccountName='" + getBankAccountName() + "'" +
            ", bankName='" + getBankName() + "'" +
            ", mdr=" + getMdr() +
            ", sharing=" + getSharing() +
            ", lastUpdatedBy='" + getLastUpdatedBy() + "'" +
            ", lastUpdatedAt='" + getLastUpdatedAt() + "'" +
            ", mallId=" + getMallId() +
            ", sharedKey='" + getSharedKey() + "'" +
            ", serviceId=" + getServiceId() +
            ", acquirerId=" + getAcquirerId() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
