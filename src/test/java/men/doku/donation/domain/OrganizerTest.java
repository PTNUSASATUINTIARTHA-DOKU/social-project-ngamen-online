package men.doku.donation.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import men.doku.donation.web.rest.TestUtil;

public class OrganizerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organizer.class);
        Organizer organizer1 = new Organizer();
        organizer1.setId(1L);
        Organizer organizer2 = new Organizer();
        organizer2.setId(organizer1.getId());
        assertThat(organizer1).isEqualTo(organizer2);
        organizer2.setId(2L);
        assertThat(organizer1).isNotEqualTo(organizer2);
        organizer1.setId(null);
        assertThat(organizer1).isNotEqualTo(organizer2);
    }
}
