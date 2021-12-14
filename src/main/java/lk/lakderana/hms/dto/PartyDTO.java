package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyDTO {

    private Long partyId;
    private String name;
    /*private String firstName;
    private String lastName;*/
    private Date dob;
    private String address1;
    private String address2;
    private String address3;
    private String gender;
    private String nic;
    private String passport;
    private String type;
    private String departmentCode;
    private Long branchId;
    private Long managedBy;
    private Short status;
}
