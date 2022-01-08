package lk.lakderana.hms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lk.lakderana.hms.repository.BranchRepository;
import lk.lakderana.hms.util.DateConversion;
import lk.lakderana.hms.util.constant.status.InquiryStatus;
import lk.lakderana.hms.util.constant.status.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDTO {

    private String partyCode;
    private String createdBy;
    private String createdDate;
    private String dateTime;
    private String remarks;
    private String rowNumber;
    private String customerName;
    private String customerContactNo;
    private String transferredToBranchName;
    private String checkInDateTime;
    private String checkOutDateTime;
    private String noOfAdults;
    private String noOfChildren;
    private String inquiryStatus;
    private String reservationStatus;
    private String reservationId;
    private String payMethod;
    private String amount;
    private String branchName;
    private String invoiceNumber;
    private String reservedType;
    private String quantity;
    private String unitPrice;
    private String attendanceDate;
    private String inTime;
    private String outTime;
    private String employeeName;

    @Autowired
    private BranchRepository branchRepository;

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = DateConversion.convertDateToStringWithTime(attendanceDate).split(" ")[0];
    }

    public void setInTime(Date inTime) {
        this.inTime =  inTime == null ? "-" : DateConversion.convertDateToStringWithTime(inTime).split(" ")[1];
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime == null ? "-" : DateConversion.convertDateToStringWithTime(outTime).split(" ")[1];
    }

    public void setDateTime(Date inquiryDateTime) {
        this.dateTime = DateConversion.convertDateToString(inquiryDateTime);
    }

    public void setInquiryStatus(Short inquiryStatus) {
        this.inquiryStatus = InquiryStatus.getNameByCode(inquiryStatus).name();
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = Strings.isNullOrEmpty(partyCode) ? "-" : partyCode;
    }

    public void setTransferredToBranchName(String transferredToBranchName) {
        this.transferredToBranchName = Strings.isNullOrEmpty(transferredToBranchName) ? "-" : transferredToBranchName;
    }

    public void setCheckInDateTime(Date checkInDateTime) {
        this.checkInDateTime =  DateConversion.convertDateToStringWithTime(checkInDateTime);
    }

    public void setCheckOutDateTime(Date checkOutDateTime) {
        this.checkOutDateTime = DateConversion.convertDateToStringWithTime(checkOutDateTime);
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = DateConversion.convertDateToString(createdDate);
    }

    public void setNoOfAdults(Integer noOfAdults) {
        this.noOfAdults = noOfAdults.toString();
    }

    public void setNoOfChildren(Integer noOfChildren) {
        this.noOfChildren = noOfChildren == null ? "-" : noOfChildren.toString();
    }

    public void setReservationStatus(Short reservationStatus) {
        this.reservationStatus = ReservationStatus.getNameByCode(reservationStatus).name();
    }

    public void setReservationId(String reservationId) {
        this.reservationId = "RSV" + reservationId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = new DecimalFormat("#,###.00").format(amount);
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity.toString();
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = new DecimalFormat("#,###.00").format(unitPrice);
    }
}
