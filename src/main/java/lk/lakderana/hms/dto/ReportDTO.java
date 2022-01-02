package lk.lakderana.hms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lk.lakderana.hms.util.DateConversion;
import lk.lakderana.hms.util.constant.status.InquiryStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.assertj.core.util.Strings;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportDTO {

    private String partyCode;
    private String inquiryDateTime;
    private String remarks;
    private String inquiryStatus;
    private String customerName;
    private String customerContactNo;
    private String rowNumber;

    public void setInquiryDateTime(Date inquiryDateTime) {
        this.inquiryDateTime = DateConversion.convertDateToString(inquiryDateTime);
    }

    public void setInquiryStatus(Short inquiryStatus) {
        this.inquiryStatus = InquiryStatus.getNameByCode(inquiryStatus).name();
    }

    public void setPartyCode(String partyCode) {
        this.partyCode = Strings.isNullOrEmpty(partyCode) ? "-" : partyCode;
    }
}
