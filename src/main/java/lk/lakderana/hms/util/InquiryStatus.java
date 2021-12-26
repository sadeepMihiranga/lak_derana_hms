package lk.lakderana.hms.util;

public enum InquiryStatus {

    CREATED((short) 1),
    RESERVED((short) 2),
    TRANSFERRED_TO_ANOTHER((short) 3),
    CANCELED((short) 4);

    private short shortValue;

    InquiryStatus(short shortValue) {
        this.shortValue = shortValue;
    }

    public short getShortValue() {
        return shortValue;
    }
}
