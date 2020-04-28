package men.doku.donation.web.rest.errors;

public class NoAuthorityException extends BadRequestAlertException  {

    private static final long serialVersionUID = 1L;

    public NoAuthorityException(String entityName, String action) {
        super(ErrorConstants.NO_AUTHORITY, "You have no authority to " + action, 
            entityName, "noauthority");
    }
}