package lk.lakderana.hms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lk.lakderana.hms.util.DateConversion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private Short inquiryStatus;
    private String customerName;
    private String customerContactNo;
    private String rowNumber;

    public void setInquiryDateTime(Date inquiryDateTime) {
        this.inquiryDateTime = DateConversion.convertDateToString(inquiryDateTime);
    }
}
