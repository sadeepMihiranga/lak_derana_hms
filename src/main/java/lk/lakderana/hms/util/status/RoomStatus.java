package lk.lakderana.hms.util.status;

public enum RoomStatus {

    INACTIVE((short) 0),
    READY_FOR_BOOKING((short) 1),
    RESERVED((short) 2),
    IN_USE((short) 3),
    ON_MAINTENANCE((short) 4);

    private short shortValue;

    RoomStatus(short shortValue) {
        this.shortValue = shortValue;
    }

    public short getShortValue() {
        return shortValue;
    }
}
