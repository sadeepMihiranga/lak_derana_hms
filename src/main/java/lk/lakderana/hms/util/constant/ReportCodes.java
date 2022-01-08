package lk.lakderana.hms.util.constant;

public enum ReportCodes {

    INQUIRY_DETAILED("RTINQDT"),
    RESERVATION_DETAILED("RTRESDT"),
    INCOME_DETAILED("RTDALIC"),
    INVOICE_WISE_INCOME_DETAILED("RTMONIC"),
    EMPLOYEE_ATTENDANCE_DETAILED("RTATTDT");

    private String value;
    ReportCodes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
