package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.dto.ReportTypeDTO;
import lk.lakderana.hms.entity.TRfReportType;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.mapper.ReportTypeMapper;
import lk.lakderana.hms.repository.ReportTypeRepository;
import lk.lakderana.hms.service.ReportTypeService;
import lk.lakderana.hms.util.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReportTypeServiceImpl implements ReportTypeService {

    private final ReportTypeRepository reportTypeRepository;

    public ReportTypeServiceImpl(ReportTypeRepository reportTypeRepository) {
        this.reportTypeRepository = reportTypeRepository;
    }

    @Override
    public List<ReportTypeDTO> getReportTypes() {

        final List<TRfReportType> tRfReportTypeList = reportTypeRepository
                .findAllByRptpStatus(Constants.STATUS_ACTIVE.getShortValue());

        List<ReportTypeDTO> reportTypeDTOList = new ArrayList<>();

        tRfReportTypeList.forEach(tRfReportType -> {

            reportTypeDTOList.add(ReportTypeMapper.INSTANCE.entityToDTO(tRfReportType));
        });

        return reportTypeDTOList;
    }

    @Override
    public ReportTypeDTO getByCode(String reportCode) {

        if(Strings.isNullOrEmpty(reportCode))
            throw new NoRequiredInfoException("Report Code is required");

        final TRfReportType tRfReportType = reportTypeRepository.findByRptpCode(reportCode);

        if(tRfReportType == null)
            throw new DataNotFoundException("Report Type not found for Code " + reportCode);

        return ReportTypeMapper.INSTANCE.entityToDTO(tRfReportType);
    }
}
