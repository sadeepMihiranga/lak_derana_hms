package lk.lak_derana_hms.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class FunctionDTO {
    private String functionId;
    private LocalDateTime funcInactiveTime;
    private String funcInactiveCode;
    private String functionName;
    private String functionStatus;
}
