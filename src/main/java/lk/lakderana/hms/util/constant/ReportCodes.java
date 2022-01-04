package lk.lakderana.hms.util.constant;

public enum ReportCodes {

    INQUIRY_DETAILED("RTINQDT"),
    RESERVATION_DETAILED("RTRESDT");

    private String value;
    ReportCodes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
