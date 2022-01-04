package lk.lakderana.hms.util.constant;

public enum CommonReferenceTypeCodes {

    PARTY_TYPES("PTYPE"),
    PARTY_CONTACT_TYPES("CONTC"),
    GENDER_TYPES("GENDR"),
    ROOM_TYPES("ROMTP"),
    ROOM_CATEGORY_TYPES("RMCAT"),
    ROOM_BED_TYPES("RMBDT"),
    FACILITY_TYPES("FCLTP"),
    MEASUREMENT_TYPES("UOFMS"),
    ITEM_TYPES("ITMTP"),
    PAYMENT_TYPES("PAYTP");

    private String value;
    private short shortValue;
    private int intValue;

    CommonReferenceTypeCodes(String value) {
        this.value = value;
    }

    CommonReferenceTypeCodes(short shortValue) {
        this.shortValue = shortValue;
    }

    CommonReferenceTypeCodes(int intValue) {
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
