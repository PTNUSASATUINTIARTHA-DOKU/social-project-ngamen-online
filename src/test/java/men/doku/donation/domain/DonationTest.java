package men.doku.donation.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import men.doku.donation.web.rest.TestUtil;

public class DonationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Donation.class);
        Donation donation1 = new Donation();
        donation1.setId(1L);
        Donation donation2 = new Donation();
        donation2.setId(donation1.getId());
        assertThat(donation1).isEqualTo(donation2);
        donation2.setId(2L);
        assertThat(donation1).isNotEqualTo(donation2);
        donation1.setId(null);
        assertThat(donation1).isNotEqualTo(donation2);
    }
}
