package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyContactDTO {

    private Long contactId;
    private Long partyId;
    private String contactType;
    private String contactNumber;
    private Short status;
}
