package men.doku.donation.service.dto;

import javax.transaction.Transaction;
import men.doku.donation.domain.Donation;

public class PaymentDTO {

    private DonationDTO donation;
    private Transaction transaction;

    public PaymentDTO() {
    }
        
    public PaymentDTO(Donation donation) {
        OrganizerDTO organizer = new OrganizerDTO(donation.getOrganizer().getId(), 
                donation.getOrganizer().getName(), donation.getOrganizer().getUrl(), 
                donation.getOrganizer().getEmail());
        DonationDTO donationDto = new DonationDTO(donation.getId(), donation.getName(), 
                donation.getDescription(), donation.getUrl(), donation.getImageUrl(), 
                donation.getPaymentSlug(), organizer);
        this.donation = donationDto;
    }

    /** 
     * @return Donation
     */
    public DonationDTO getDonation() {
        return donation;
    }

    
    /** 
     * @param donation
     */
    public void setDonation(DonationDTO donation) {
        this.donation = donation;
    }

    
    /** 
     * @return Transaction
     */
    public Transaction getTransaction() {
        return transaction;
    }

    
    /** 
     * @param transaction
     */
    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}