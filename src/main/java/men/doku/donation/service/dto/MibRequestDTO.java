package men.doku.donation.service.dto;

public class MibRequestDTO {

    private String MALLID;
    private String SERVICEID;
    private String ACQUIRERID;
    private String INVOICENUMBER;
    private String CURRENCY;
    private String AMOUNT;
    private String SESSIONID;
    private String AUTH1;
    private String BASKET;
    private String WORDS;

    public MibRequestDTO() {
    }

    public MibRequestDTO(String mALLID, String sERVICEID, String aCQUIRERID, String iNVOICENUMBER, String cURRENCY,
    String aMOUNT, String sESSIONID, String aUTH1, String bASKET, String wORDS) {
        this.MALLID = mALLID;
        this.SERVICEID = sERVICEID;
        this.ACQUIRERID = aCQUIRERID;
        this.INVOICENUMBER = iNVOICENUMBER;
        this.CURRENCY = cURRENCY;
        this.AMOUNT = aMOUNT;
        this.SESSIONID = sESSIONID;
        this.AUTH1 = aUTH1;
        this.BASKET = bASKET;
        this.WORDS = wORDS;
    }

    public String getMALLID() {
        return MALLID;
    }

    public void setMALLID(String mALLID) {
        this.MALLID = mALLID;
    }

    public String getSERVICEID() {
        return SERVICEID;
    }

    public void setSERVICEID(String sERVICEID) {
        this.SERVICEID = sERVICEID;
    }

    public String getACQUIRERID() {
        return ACQUIRERID;
    }

    public void setACQUIRERID(String aCQUIRERID) {
        this.ACQUIRERID = aCQUIRERID;
    }

    public String getINVOICENUMBER() {
        return INVOICENUMBER;
    }

    public void setINVOICENUMBER(String iNVOICENUMBER) {
        this.INVOICENUMBER = iNVOICENUMBER;
    }

    public String getCURRENCY() {
        return CURRENCY;
    }

    public void setCURRENCY(String cURRENCY) {
        this.CURRENCY = cURRENCY;
    }

    public String getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(String aMOUNT) {
        this.AMOUNT = aMOUNT;
    }

    public String getSESSIONID() {
        return SESSIONID;
    }

    public void setSESSIONID(String sESSIONID) {
        this.SESSIONID = sESSIONID;
    }

    public String getAUTH1() {
        return AUTH1;
    }

    public void setAUTH1(String aUTH1) {
        this.AUTH1 = aUTH1;
    }

    public String getBASKET() {
        return BASKET;
    }

    public void setBASKET(String bASKET) {
        this.BASKET = bASKET;
    }

    public String getWORDS() {
        return WORDS;
    }

    public void setWORDS(String wORDS) {
        this.WORDS = wORDS;
    }

    @Override
    public String toString() {
        return "MibRequestDTO [ACQUIRERID=" + ACQUIRERID + ", AMOUNT=" + AMOUNT + ", AUTH1=" + AUTH1 + ", BASKET="
                + BASKET + ", CURRENCY=" + CURRENCY + ", INVOICENUMBER=" + INVOICENUMBER + ", MALLID=" + MALLID
                + ", SERVICEID=" + SERVICEID + ", SESSIONID=" + SESSIONID + ", WORDS=" + WORDS + "]";
    }
}
