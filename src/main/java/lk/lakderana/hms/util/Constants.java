package lk.lakderana.hms.util;

public enum Constants {

    /** status */
    STATUS_ACTIVE((short) 1),
    STATUS_INACTIVE((short) 0),

    /** party types */
    PARTY_TYPE_CUSTOMER(""),
    PARTY_TYPE_EMPLOYEE("");

    private String value;
    private short shortValue;
    private int intValue;

    Constants(String value) {
        this.value = value;
    }

    Constants(short shortValue) {
        this.shortValue = shortValue;
    }

    Constants(int intValue) {
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
