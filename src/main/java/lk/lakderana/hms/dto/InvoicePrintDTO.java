package lk.lakderana.hms.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.net.URL;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InvoicePrintDTO {

    private URL logoImage;
    private String invoiceNumber;
    private String branchName;
    private String createdDate;
    private String checkInDateTime;
    private String checkOutDateTime;
    private String noOfDays;
    private String noOfAdults;
    private String noOfChildren;
    private String customerName;
    private String customerContactNo;
    private String customerAddress;
    private String customerCode;
    private String grossAmount;
    private String netAmount;
    private String discountAmount;
    private String taxAmount;
    private JRBeanCollectionDataSource reservationDetailsDataSource;
}
