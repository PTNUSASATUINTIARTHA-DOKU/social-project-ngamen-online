package men.doku.donation.service.dto;

import javax.transaction.Transaction;

import men.doku.donation.domain.Donation;

public class PaymentDTO {

    private String slug;
    private Donation donation;
    private Transaction transaction;

    public PaymentDTO() {
    }
    
    /** 
     * @return String
     */
    public String getSlug() {
        return slug;
    }

    
    /** 
     * @param slug
     */
    public void setSlug(String slug) {
        this.slug = slug;
    }

    
    /** 
     * @return Donation
     */
    public Donation getDonation() {
        return donation;
    }

    
    /** 
     * @param donation
     */
    public void setDonation(Donation donation) {
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