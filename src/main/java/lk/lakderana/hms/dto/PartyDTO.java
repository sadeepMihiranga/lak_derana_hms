package lk.lakderana.hms.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyDTO implements Paginated {

    private Long partyId;
    private String name;
    private String firstName;
    private String lastName;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    private List<PartyContactDTO> contactList;
}