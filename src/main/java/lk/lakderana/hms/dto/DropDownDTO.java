package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DropDownDTO {

    private String key;
    private String value;
    private Short status;
}
