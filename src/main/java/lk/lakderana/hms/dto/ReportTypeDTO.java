package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportTypeDTO {

    private Long reportTypeId;
    private String reportTypeCode;
    private String displayName;
    private String icon;
    private String color;
    private Short status;
}
