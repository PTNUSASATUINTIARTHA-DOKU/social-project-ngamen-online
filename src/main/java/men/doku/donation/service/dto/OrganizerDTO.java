package men.doku.donation.service.dto;

public class OrganizerDTO {

    private Long id;
    private String name;
    private String url;
    private String email;

    public OrganizerDTO() {
    }

    public OrganizerDTO(Long id, String name, String url, String email) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.email = email;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "OrganizerDTO [email=" + email + ", id=" + id + ", name=" + name + ", url=" + url + "]";
    }
}
