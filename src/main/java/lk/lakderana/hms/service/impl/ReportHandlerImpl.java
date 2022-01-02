package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.ReportDTO;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.service.ReportHandler;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class ReportHandlerImpl extends EntityValidator implements ReportHandler {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ReportDTO> getInquiryReportContent(Date fromDate, Date toDate) {

        if (fromDate == null || toDate == null)
            throw new DataNotFoundException("From Date and To Date is Required.");


        Query query = entityManager.createNativeQuery(
                "SELECT CONCAT('#', row_number() OVER (ORDER BY \"inq\".\"INQR_DATE_TIME\")) AS \"rowNumber\",\n " +
                        "\"inq\".\"INQR_CUSTOMER_CODE\" AS \"partyCode\", \"inq\".\"INQR_DATE_TIME\" AS \"dateTime\",\n " +
                        "\"inq\".\"INQR_REMARKS\" AS \"remarks\", \"inq\".\"INQR_STATUS\" AS \"inquiryStatus\",\n " +
                        "\"inq\".\"INQR_CUSTOMER_NAME\" AS \"customerName\", \"inq\".\"INQR_CONTACT_NO\" AS \"customerContactNo\",\n " +
                        "\"brnh\".\"BRNH_NAME\" AS \"transferredToBranchName\"\n " +
                        "FROM \"LAKDERANA_BASE\".\"T_MS_INQUIRY\" \"inq\"\n " +
                        "LEFT JOIN \"LAKDERANA_BASE\".\"T_RF_BRANCH\" \"brnh\" ON inq.\"INQR_TRANSFERRED_TO\" = \"brnh\".\"BRNH_ID\"\n " +
                        "WHERE \"CREATED_DATE\" BETWEEN :fromDate AND :toDate");
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        try {
            query.unwrap(org.hibernate.query.NativeQuery.class)
                    .setResultTransformer(Transformers.aliasToBean(ReportDTO.class));
            return (List<ReportDTO>) query.getResultList();
        } catch (NoResultException e) {
            log.error("Report Service -> ReportHandlerImpl -> getInquiryReportContent : {} ", e.getMessage());
        }
        return Collections.emptyList();
    }
}
