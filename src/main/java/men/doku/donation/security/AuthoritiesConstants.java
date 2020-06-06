package men.doku.donation.security;

import java.util.HashMap;
import java.util.Map;

/**
 * Constants for Spring Security authorities.
 */
public class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String USER = "ROLE_USER";
    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
    public static final String OFFLINE_STORE = "ROLE_OFFLINE_STORE";

    public enum Role {
        ROLE_ADMIN,
        ROLE_USER,
        ROLE_ANONYMOUS,
        ROLE_OFFLINE_STORE
    }    

    public static final Map<Role, Class<?>> MAPPING = new HashMap<>();

    static {
        MAPPING.put(Role.ROLE_ADMIN, Admin.class);
        MAPPING.put(Role.ROLE_USER, User.class);
        MAPPING.put(Role.ROLE_ANONYMOUS, Anonymous.class);
        MAPPING.put(Role.ROLE_OFFLINE_STORE, OfflineStore.class);
    }

    public static class Anonymous {
    }

    public static class User extends Anonymous {
    }

    public static class Admin extends User {
    }

    public static class OfflineStore extends User {
    }
}

