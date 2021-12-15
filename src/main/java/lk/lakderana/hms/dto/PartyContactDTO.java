package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyContactDTO {

    private Long contactId;
    private Long partyId;
    @NotBlank(message = "Contact Type is mandatory")
    private String contactType;
    @NotBlank(message = "Contact Number is mandatory")
    private String contactNumber;
    private Short status;
}
