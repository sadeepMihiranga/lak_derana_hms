package lk.lakderana.hms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO implements Paginated {

    private Long reservationId;
    private Long inquiryId;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkInDateTime;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkOutDateTime;
    private String remarks;
    @NotBlank(message = "No of Adults is required")
    private Integer noOfAdults;
    private Integer noOfChildren;
    private Short status;
    private Long branchId;
    private String branchName;
    private String customerName;
    private String customerContactNo;
    private LocalDateTime createdDate;
    private String createdUserCode;
    private LocalDateTime lastUpdatedDate;
    private String lastUpdatedUserCode;
    private String cancellationReasons;
    private String paymentType;
    private BigDecimal advancePayment;
    private BigDecimal totalAmount;
    private BigDecimal dueAmount;
    private BigDecimal paidAmount;
    private List<RoomReservationDTO> roomReservationList;
    private List<FacilityReservationDTO> facilityReservationList;
    private List<ItemReservationDTO> itemReservationList;
}
