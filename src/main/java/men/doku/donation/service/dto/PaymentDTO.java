package men.doku.donation.service.dto;

import men.doku.donation.domain.Donation;
import men.doku.donation.domain.Transaction;

public class PaymentDTO {

    private DonationDTO donation;
    private Transaction transaction;

    /**
     * Constructor
     */
    public PaymentDTO() {
    }
        
    /**
     * Constructor with 1 parameter
     * 
     * @param donation object Donation
     */
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
     * Constructor with two params
     * 
     * @param donation object DonationDTO
     * @param transaction object Transactions
     */
    public PaymentDTO(DonationDTO donation, Transaction transaction) {
        this.donation = donation;
        this.transaction = transaction;
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