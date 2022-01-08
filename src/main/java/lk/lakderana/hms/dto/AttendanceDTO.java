package lk.lakderana.hms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceDTO implements Paginated {

    private Long attendanceId;
    @NotBlank(message = "Employee Code is required")
    private String employeeCode;
    private String employeeName;
    @NotNull(message = "Attendance Date is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate attendanceDate;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime inTime;
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime outTime;
    private Short status;
    private Long branchId;
    private String branchName;
}
