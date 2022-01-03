package lk.lakderana.hms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportHistoryDTO implements Paginated {

    private Long reportHistoryId;
    private String reportType;
    private String reportDisplayName;
    private Short status;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private LocalDateTime createdDate;
    private String createdUserCode;
    private String createdUserName;
    private LocalDateTime lastUpdatedDate;
    private String lastUpdatedUserCode;
    private Long branchId;
}
