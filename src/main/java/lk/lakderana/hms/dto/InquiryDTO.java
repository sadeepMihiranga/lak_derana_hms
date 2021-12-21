package lk.lakderana.hms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDTO implements Paginated {

    private Long inquiryId;
    private String partyCode;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime inquiryDateTime;
    private String remarks;
    private Short inquiryStatus;
    @NotNull(message = "Branch is required")
    private Long branchId;
    @NotBlank(message = "Customer Name is required")
    private String customerName;
    @NotBlank(message = "Customer Contact Number is required")
    private String customerContactNo;
}
