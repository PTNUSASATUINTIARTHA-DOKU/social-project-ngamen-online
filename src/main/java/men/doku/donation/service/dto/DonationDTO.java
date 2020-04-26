package men.doku.donation.service.dto;

public class DonationDTO {

    private Long id;
    private String name;
    private String description;
    private String url;
    private String imageUrl;
    private String paymentSlug;
    private OrganizerDTO organizer;

    /**
     * Constructor 
     */
    public DonationDTO() { 
    }

    /**
     * Constructor with 7 params
     * 
     * @param id
     * @param name
     * @param description
     * @param url
     * @param imageUrl
     * @param paymentSlug
     * @param organizer
     */
    public DonationDTO(Long id, String name, String description, String url, String imageUrl, String paymentSlug, OrganizerDTO organizer) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.imageUrl = imageUrl;
        this.paymentSlug = paymentSlug;
        this.organizer = organizer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPaymentSlug() {
        return paymentSlug;
    }

    public void setPaymentSlug(String paymentSlug) {
        this.paymentSlug = paymentSlug;
    }

    public OrganizerDTO getOrganizer() {
        return organizer;
    }

    public void setOrganizer(OrganizerDTO organizer) {
        this.organizer = organizer;
    }

    @Override
    public String toString() {
        return "DonationDTO [description=" + description + ", id=" + id + ", imageUrl=" + imageUrl + ", name=" + name
                + ", organizer=" + organizer + ", paymentSlug=" + paymentSlug + ", url=" + url + "]";
    }
}
