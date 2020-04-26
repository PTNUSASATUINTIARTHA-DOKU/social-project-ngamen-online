package men.doku.donation.service.dto;

import javax.validation.constraints.NotNull;

public class MibRequestDTO {

    @NotNull
    private Integer MALLID;

    @NotNull
    private Integer SERVICEID;

    @NotNull
    private Integer ACQUIRERID;

    @NotNull
    private String INVOICENUMBER;

    @NotNull
    private String CURRENCY;

    @NotNull
    private Long AMOUNT;

    @NotNull
    private String SESSIONID;

    @NotNull
    private String AUTH1;

    @NotNull
    private String BASKET;

    @NotNull
    private String WORDS;

    public MibRequestDTO() {
    }

    public MibRequestDTO(Integer mALLID, Integer sERVICEID, Integer aCQUIRERID, String iNVOICENUMBER, String cURRENCY,
            Long aMOUNT, String sESSIONID, String aUTH1, String bASKET, String wORDS) {
        MALLID = mALLID;
        SERVICEID = sERVICEID;
        ACQUIRERID = aCQUIRERID;
        INVOICENUMBER = iNVOICENUMBER;
        CURRENCY = cURRENCY;
        AMOUNT = aMOUNT;
        SESSIONID = sESSIONID;
        AUTH1 = aUTH1;
        BASKET = bASKET;
        WORDS = wORDS;
    }

    public Integer getMALLID() {
        return MALLID;
    }

    public void setMALLID(Integer mALLID) {
        MALLID = mALLID;
    }

    public Integer getSERVICEID() {
        return SERVICEID;
    }

    public void setSERVICEID(Integer sERVICEID) {
        SERVICEID = sERVICEID;
    }

    public Integer getACQUIRERID() {
        return ACQUIRERID;
    }

    public void setACQUIRERID(Integer aCQUIRERID) {
        ACQUIRERID = aCQUIRERID;
    }

    public String getINVOICENUMBER() {
        return INVOICENUMBER;
    }

    public void setINVOICENUMBER(String iNVOICENUMBER) {
        INVOICENUMBER = iNVOICENUMBER;
    }

    public String getCURRENCY() {
        return CURRENCY;
    }

    public void setCURRENCY(String cURRENCY) {
        CURRENCY = cURRENCY;
    }

    public Long getAMOUNT() {
        return AMOUNT;
    }

    public void setAMOUNT(Long aMOUNT) {
        AMOUNT = aMOUNT;
    }

    public String getSESSIONID() {
        return SESSIONID;
    }

    public void setSESSIONID(String sESSIONID) {
        SESSIONID = sESSIONID;
    }

    public String getAUTH1() {
        return AUTH1;
    }

    public void setAUTH1(String aUTH1) {
        AUTH1 = aUTH1;
    }

    public String getBASKET() {
        return BASKET;
    }

    public void setBASKET(String bASKET) {
        BASKET = bASKET;
    }

    public String getWORDS() {
        return WORDS;
    }

    public void setWORDS(String wORDS) {
        WORDS = wORDS;
    }
}