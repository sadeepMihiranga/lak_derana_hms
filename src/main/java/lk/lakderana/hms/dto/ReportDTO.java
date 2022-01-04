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
    private String noOfPersons;
    private String inquiryStatus;
    private String reservationStatus;

    @Autowired
    private BranchRepository branchRepository;

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

    public void setNoOfPersons(Integer noOfPersons) {
        this.noOfPersons = noOfPersons.toString();
    }

    public void setReservationStatus(Short reservationStatus) {
        this.reservationStatus = ReservationStatus.getNameByCode(reservationStatus).name();
    }
}
