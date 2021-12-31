package lk.lakderana.hms.util.constant.status;

public enum ReservationStatus {

    CANCELLED((short) 0),
    CONFIRMED((short) 1),
    RELEASE((short) 2);

    private short shortValue;

    ReservationStatus(short shortValue) {
        this.shortValue = shortValue;
    }

    public short getShortValue() {
        return shortValue;
    }
}
