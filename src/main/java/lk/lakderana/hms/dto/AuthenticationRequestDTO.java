package lk.lakderana.hms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@JsonIgnoreProperties
public class AuthenticationRequestDTO {

    private String username;
    private String password;
}
