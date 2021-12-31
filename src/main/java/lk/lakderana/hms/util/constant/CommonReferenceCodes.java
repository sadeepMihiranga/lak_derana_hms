package lk.lakderana.hms.util.constant;

public enum CommonReferenceCodes {

    /** party types */
    PARTY_TYPE_CUSTOMER("CUSTM"),
    PARTY_TYPE_EMPLOYEE("EMPLY"),

    /** party contact types */
    PARTY_CONTACT_MOBILE("CNMBL"),
    PARTY_CONTACT_EMAIL("CNEML");

    private String value;
    private short shortValue;
    private int intValue;

    CommonReferenceCodes(String value) {
        this.value = value;
    }

    CommonReferenceCodes(short shortValue) {
        this.shortValue = shortValue;
    }

    CommonReferenceCodes(int intValue) {
        this.intValue = intValue;
    }

    public String getValue() {
        return value;
    }

    public short getShortValue() {
        return shortValue;
    }

    public int getIntValue() {
        return intValue;
    }
}
