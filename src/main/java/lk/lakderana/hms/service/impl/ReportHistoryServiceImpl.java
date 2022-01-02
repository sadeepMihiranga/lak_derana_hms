package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.ReportHistoryDTO;
import lk.lakderana.hms.dto.ReportTypeDTO;
import lk.lakderana.hms.entity.TTrReportHistory;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.ReportHistoryMapper;
import lk.lakderana.hms.mapper.ReportTypeMapper;
import lk.lakderana.hms.repository.ReportHistoryRepository;
import lk.lakderana.hms.service.ReportHistoryService;
import lk.lakderana.hms.service.ReportTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import static lk.lakderana.hms.util.constant.Constants.*;

@Slf4j
@Service
public class ReportHistoryServiceImpl extends EntityValidator implements ReportHistoryService {

    private final ReportHistoryRepository reportHistoryRepository;

    private final ReportTypeService reportTypeService;

    public ReportHistoryServiceImpl(ReportHistoryRepository reportHistoryRepository,
                                    ReportTypeService reportTypeService) {
        this.reportHistoryRepository = reportHistoryRepository;
        this.reportTypeService = reportTypeService;
    }

    @Override
    public ReportHistoryDTO createReportHistory(ReportHistoryDTO reportHistoryDTO) {

        if(reportHistoryDTO == null)
            throw new NoRequiredInfoException("No required Data");

        final ReportTypeDTO reportTypeDTO = reportTypeService.getByCode(reportHistoryDTO.getReportType());

        final TTrReportHistory tTrReportHistory = ReportHistoryMapper.INSTANCE.dtoToEntity(reportHistoryDTO);
        tTrReportHistory.setRphtStatus(STATUS_ACTIVE.getShortValue());
        tTrReportHistory.setReportType(ReportTypeMapper.INSTANCE.dtoToEntity(reportTypeDTO));

        return ReportHistoryMapper.INSTANCE.entityToDTO(persistEntity(tTrReportHistory));
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
