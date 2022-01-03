package lk.lakderana.hms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReportContentWrapperDTO {

    private JRBeanCollectionDataSource reportDatasource;
    private String startDate;
    private String endDate;
    private String reportName;
}
