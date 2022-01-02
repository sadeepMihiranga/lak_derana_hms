package lk.lakderana.hms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lk.lakderana.hms.entity.TRfBranch;
import lk.lakderana.hms.repository.BranchRepository;
import lk.lakderana.hms.util.DateConversion;
import lk.lakderana.hms.util.constant.status.InquiryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static lk.lakderana.hms.util.constant.Constants.STATUS_ACTIVE;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDTO {

    private String partyCode;
    private String dateTime;
    private String remarks;
    private String inquiryStatus;
    private String customerName;
    private String customerContactNo;
    private String rowNumber;
    private String transferredToBranchName;

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
}
