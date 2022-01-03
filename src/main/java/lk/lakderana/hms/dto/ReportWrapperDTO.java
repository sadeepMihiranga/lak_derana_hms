package lk.lakderana.hms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.sf.jasperreports.engine.JasperPrint;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportWrapperDTO {

    private JasperPrint jasperPrint;
    private String reportFileNameWithExtension;
}