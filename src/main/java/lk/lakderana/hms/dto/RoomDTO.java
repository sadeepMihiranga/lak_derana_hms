package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO implements Paginated {

    private Long roomId;
    @NotBlank(message = "Room Type is required")
    private String roomType;
    private String roomNo;
    private String roomDescription;
    private String roomTypeName;
    @NotBlank(message = "Room Category is required")
    private String roomCategory;
    private String roomCategoryName;
    private BigDecimal roomPrice;
    private String roomBedType;
    private String roomBedTypeName;
    private Short status;
    private Long branchId;
    private String branchName;
    private LocalDateTime createdDate;
    private String createdUserCode;
    private LocalDateTime lastUpdatedDate;
    private String lastUpdatedUserCode;
}
