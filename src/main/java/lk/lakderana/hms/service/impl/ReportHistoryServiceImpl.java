package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.*;
import lk.lakderana.hms.entity.TTrReportHistory;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.ReportHistoryMapper;
import lk.lakderana.hms.mapper.ReportTypeMapper;
import lk.lakderana.hms.repository.ReportHistoryRepository;
import lk.lakderana.hms.service.PartyService;
import lk.lakderana.hms.service.ReportHistoryService;
import lk.lakderana.hms.service.ReportService;
import lk.lakderana.hms.service.ReportTypeService;
import lk.lakderana.hms.util.DateConversion;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static lk.lakderana.hms.util.constant.Constants.STATUS_ACTIVE;
import static lk.lakderana.hms.util.constant.Constants.STATUS_INACTIVE;

@Slf4j
@Service
public class ReportHistoryServiceImpl extends EntityValidator implements ReportHistoryService {

    private final ReportHistoryRepository reportHistoryRepository;

    private final ReportTypeService reportTypeService;
    private final ReportService reportService;
    private final PartyService partyService;

    public ReportHistoryServiceImpl(ReportHistoryRepository reportHistoryRepository,
                                    ReportTypeService reportTypeService,
                                    @Lazy ReportService reportService,
                                    PartyService partyService) {
        this.reportHistoryRepository = reportHistoryRepository;
        this.reportTypeService = reportTypeService;
        this.reportService = reportService;
        this.partyService = partyService;
    }

    @Override
    public ReportHistoryDTO createReportHistory(ReportHistoryDTO reportHistoryDTO) {

        if(reportHistoryDTO == null)
            throw new NoRequiredInfoException("No required Data");

        final ReportTypeDTO reportTypeDTO = reportTypeService.getByCode(reportHistoryDTO.getReportType());

        reportHistoryDTO.setBranchId(captureBranchIds().get(0));
        final TTrReportHistory tTrReportHistory = ReportHistoryMapper.INSTANCE.dtoToEntity(reportHistoryDTO);

        tTrReportHistory.setRphtStatus(STATUS_ACTIVE.getShortValue());
        tTrReportHistory.setReportType(ReportTypeMapper.INSTANCE.dtoToEntity(reportTypeDTO));

        return ReportHistoryMapper.INSTANCE.entityToDTO(persistEntity(tTrReportHistory));
    }

    @Override
    public PaginatedEntity reportHistoryPaginatedSearch(String reportTypeCode, Integer page, Integer size) {

        PaginatedEntity paginatedReportHistoryList = null;
        List<ReportHistoryDTO> reportHistoryDTOList = null;

        validatePaginateIndexes(page, size);

        reportTypeCode = reportTypeCode.isEmpty() ? null : reportTypeCode;

        final Page<TTrReportHistory> tTrReportHistoryPage = reportHistoryRepository
                .getReportHistory(reportTypeCode, STATUS_ACTIVE.getShortValue(), captureBranchIds(),
                        PageRequest.of(page - 1, size));

        if (tTrReportHistoryPage.getSize() == 0)
            return null;

        paginatedReportHistoryList = new PaginatedEntity();
        reportHistoryDTOList = new ArrayList<>();

        for (TTrReportHistory tTrReportHistory : tTrReportHistoryPage) {

            final ReportHistoryDTO reportHistoryDTO = ReportHistoryMapper.INSTANCE.entityToDTO(tTrReportHistory);

            final PartyDTO createdUserDTO = partyService.getPartyByPartyCode(reportHistoryDTO.getCreatedUserCode());
            reportHistoryDTO.setCreatedUserName(createdUserDTO.getName());

            reportHistoryDTOList.add(reportHistoryDTO);
        }

        paginatedReportHistoryList.setTotalNoOfPages(tTrReportHistoryPage.getTotalPages());
        paginatedReportHistoryList.setTotalNoOfRecords(tTrReportHistoryPage.getTotalElements());
        paginatedReportHistoryList.setEntities(reportHistoryDTOList);

        return paginatedReportHistoryList;
    }

    @Override
    public Boolean removeReportHistory(Long reportHistoryId) {

        TTrReportHistory tTrReportHistory = validateReportHistoryId(reportHistoryId);

        tTrReportHistory.setRphtStatus(STATUS_INACTIVE.getShortValue());

        persistEntity(tTrReportHistory);

        return true;
    }

    @Override
    public ReportWrapperDTO regenerateDetailedCsvReport(Long reportHistoryId) throws IOException, JRException, ParseException {

        TTrReportHistory tTrReportHistory = validateReportHistoryId(reportHistoryId);

        final ReportWrapperDTO reportWrapperDTO = reportService.generateDetailedCsvReport(
                DateConversion.convertLocalDateTimeToString(tTrReportHistory.getRphtFromDate()).split(" ")[0],
                DateConversion.convertLocalDateTimeToString(tTrReportHistory.getRphtToDate()).split(" ")[0],
                "EXCEL",
                tTrReportHistory.getReportType().getRptpCode());

        return reportWrapperDTO;
    }

    private TTrReportHistory validateReportHistoryId(Long reportHistoryId) {

        if(reportHistoryId == null)
            throw new NoRequiredInfoException("Report History Id is required");

        final TTrReportHistory tTrReportHistory = reportHistoryRepository
                .findByRphtIdAndRphtStatusAndBranch_BrnhIdIn(reportHistoryId, STATUS_ACTIVE.getShortValue(), captureBranchIds());

        if(tTrReportHistory == null)
            throw new DataNotFoundException("Report History not found for the Id " + reportHistoryId);

        return tTrReportHistory;
    }

    private TTrReportHistory persistEntity(TTrReportHistory tTrReportHistory) {
        try {
            validateEntity(tTrReportHistory);
            return reportHistoryRepository.save(tTrReportHistory);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TransactionConflictException("Transaction Updated by Another User.");
        } catch (Exception e) {
            log.error("Error while persisting : " + e.getMessage());
            throw new OperationException(e.getMessage());
        }
    }
}
