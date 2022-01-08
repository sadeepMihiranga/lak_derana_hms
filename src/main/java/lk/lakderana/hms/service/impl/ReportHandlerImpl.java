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

        validateReportRequestDates(fromDate, toDate);

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

    @Override
    public List<ReportDTO> getReservationReportContent(Date fromDate, Date toDate) {

        validateReportRequestDates(fromDate, toDate);

        Query query = entityManager.createNativeQuery(
                "SELECT CONCAT('#', row_number() OVER (ORDER BY rsv.\"RESV_ID\")) AS \"rowNumber\",\n" +
                        "       rsv.\"RESV_CHECK_IN_DATE_TIME\" AS \"checkInDateTime\",\n" +
                        "       rsv.\"RESV_CHECK_OUT_DATE_TIME\" AS \"checkOutDateTime\",\n" +
                        "       rsv.\"RESV_REMARKS\" AS \"remarks\",\n" +
                        "       rsv.\"RESV_NO_OF_ADULTS\" AS \"noOfAdults\",\n" +
                        "       rsv.\"RESV_NO_OF_CHILDREN\" AS \"noOfChildren\",\n" +
                        "       rsv.\"RESV_STATUS\" AS \"reservationStatus\",\n" +
                        "       rsv.\"CREATED_DATE\" AS \"createdDate\",\n" +
                        "       rsv.\"CREATED_USER_CODE\" AS \"createdBy\",\n" +
                        "       inq.\"INQR_CUSTOMER_CODE\" AS \"partyCode\",\n" +
                        "       CASE WHEN prt.\"PRTY_NAME\" IS NULL THEN inq.\"INQR_CUSTOMER_NAME\" ELSE prt.\"PRTY_NAME\" END AS \"customerName\"\n" +
                        "FROM \"LAKDERANA_BASE\".\"T_MS_RESERVATION\" rsv\n" +
                        "LEFT JOIN \"LAKDERANA_BASE\".\"T_MS_INQUIRY\" inq ON rsv.\"RESV_INQUIRY_ID\" = inq.\"INQR_ID\"\n" +
                        "LEFT JOIN \"LAKDERANA_BASE\".\"T_MS_PARTY\" prt ON inq.\"INQR_CUSTOMER_CODE\" = prt.\"PRTY_CODE\"\n" +
                        "WHERE inq.\"CREATED_DATE\" BETWEEN :fromDate AND :toDate");
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        try {
            query.unwrap(org.hibernate.query.NativeQuery.class)
                    .setResultTransformer(Transformers.aliasToBean(ReportDTO.class));
            return (List<ReportDTO>) query.getResultList();
        } catch (NoResultException e) {
            log.error("Report Service -> ReportHandlerImpl -> getReservationReportContent : {} ", e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public List<ReportDTO> getIncomeDetailedReportContent(Date fromDate, Date toDate) {

        validateReportRequestDates(fromDate, toDate);

        Query query = entityManager.createNativeQuery(
                "SELECT CONCAT('#', row_number() OVER (ORDER BY pay.\"PAYT_ID\")) AS \"rowNumber\",\n" +
                        "       pay.\"CREATED_DATE\" as \"createdDate\",\n" +
                        "       CAST (pay.\"PAYT_RESERVATION_ID\" AS VARCHAR) as \"reservationId\",\n" +
                        "       com.\"CMRF_DESCRIPTION\" as \"payMethod\",\n" +
                        "       pay.\"PAYT_AMOUNT\" as \"amount\",\n" +
                        "       bnh.\"BRNH_NAME\" as \"branchName\",\n" +
                        "       prt.\"PRTY_NAME\" as \"createdBy\"\n" +
                        "FROM \"LAKDERANA_BASE\".\"T_TR_PAYMENT\" pay\n" +
                        "INNER JOIN \"LAKDERANA_BASE\".\"T_RF_COMMON_REFERENCE\" com ON pay.\"PAYT_TYPE\" = com.\"CMRF_CODE\"\n" +
                        "INNER JOIN \"LAKDERANA_BASE\".\"T_RF_BRANCH\" bnh ON pay.\"PAYT_BRANCH_ID\" = bnh.\"BRNH_ID\"\n" +
                        "INNER JOIN \"LAKDERANA_BASE\".\"T_MS_PARTY\" prt ON pay.\"CREATED_USER_CODE\" = prt.\"PRTY_CODE\"\n" +
                        "WHERE pay.\"CREATED_DATE\" BETWEEN :fromDate AND :toDate \n" +
                        "  AND pay.\"PAYT_STATUS\" = 1\n" +
                        "ORDER BY pay.\"PAYT_BRANCH_ID\", pay.\"CREATED_DATE\";");
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        try {
            query.unwrap(org.hibernate.query.NativeQuery.class)
                    .setResultTransformer(Transformers.aliasToBean(ReportDTO.class));
            return (List<ReportDTO>) query.getResultList();
        } catch (NoResultException e) {
            log.error("Report Service -> ReportHandlerImpl -> getReservationReportContent : {} ", e.getMessage());
        }
        return Collections.emptyList();
    }

    @Override
    public List<ReportDTO> getInvoiceWiseIncomeDetailedReportContent(Date fromDate, Date toDate) {
        validateReportRequestDates(fromDate, toDate);

        Query query = entityManager.createNativeQuery(
                "SELECT CONCAT('#', row_number() OVER (ORDER BY invd.\"INDT_ID\")) AS \"rowNumber\",\n" +
                        "       inv.\"INVC_NUMBER\" as \"invoiceNumber\",\n" +
                        "       CAST (invd.\"INDT_RESERVATION_ID\" AS VARCHAR) as \"reservationId\",\n" +
                        "       CASE WHEN (invd.\"INDT_ROOM_RESERVATION_ID\" IS NOT NULL) AND (invd.\"INDT_FACILITY_RESERVATION_ID\" IS NULL) AND (invd.\"INDT_ITEM_RESERVATION_ID\" IS NULL) THEN 'ROOM'\n" +
                        "            WHEN (invd.\"INDT_ROOM_RESERVATION_ID\" IS NULL) AND (invd.\"INDT_FACILITY_RESERVATION_ID\" IS NOT NULL) AND (invd.\"INDT_ITEM_RESERVATION_ID\" IS NULL) THEN 'FACILITY'\n" +
                        "            WHEN (invd.\"INDT_ROOM_RESERVATION_ID\" IS NULL) AND (invd.\"INDT_FACILITY_RESERVATION_ID\" IS NULL) AND (invd.\"INDT_ITEM_RESERVATION_ID\" IS NOT NULL) THEN 'ITEM'\n" +
                        "       ELSE '' END AS \"reservedType\",\n" +
                        "       invd.\"INDT_RESERVED_QUANTITY\" as \"quantity\",\n" +
                        "       invd.\"INDT_UNIT_PRICE\" as \"unitPrice\",\n" +
                        "       invd.\"INDT_AMOUNT\" as \"amount\",\n" +
                        "       bnh.\"BRNH_NAME\" as \"branchName\",\n" +
                        "       prt.\"PRTY_NAME\" as \"createdBy\"\n" +
                        "FROM \"LAKDERANA_BASE\".\"T_TR_INVOICE_DET\" invd\n" +
                        "INNER JOIN \"LAKDERANA_BASE\".\"T_TR_INVOICE\" inv ON invd.\"INDT_INVOICE_ID\" = inv.\"INVC_ID\"\n" +
                        "INNER JOIN \"LAKDERANA_BASE\".\"T_RF_BRANCH\" bnh ON invd.\"INDT_BRANCH_ID\" = bnh.\"BRNH_ID\"\n" +
                        "INNER JOIN \"LAKDERANA_BASE\".\"T_MS_PARTY\" prt ON invd.\"CREATED_USER_CODE\" = prt.\"PRTY_CODE\"\n" +
                        "WHERE invd.\"CREATED_DATE\" BETWEEN :fromDate AND :toDate\n" +
                        "AND invd.\"INDT_STATUS\" = 1");
        query.setParameter("fromDate", fromDate);
        query.setParameter("toDate", toDate);
        try {
            query.unwrap(org.hibernate.query.NativeQuery.class)
                    .setResultTransformer(Transformers.aliasToBean(ReportDTO.class));
            return (List<ReportDTO>) query.getResultList();
        } catch (NoResultException e) {
            log.error("Report Service -> ReportHandlerImpl -> getReservationReportContent : {} ", e.getMessage());
        }
        return Collections.emptyList();
    }

    private void validateReportRequestDates(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null)
            throw new DataNotFoundException("From Date and To Date is Required.");
    }
}
