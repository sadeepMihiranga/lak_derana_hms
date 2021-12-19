package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.InquiryDTO;
import lk.lakderana.hms.entity.TRfInquiry;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.InquiryMapper;
import lk.lakderana.hms.repository.InquiryRepository;
import lk.lakderana.hms.service.InquiryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static lk.lakderana.hms.util.Constants.STATUS_ACTIVE;

@Slf4j
@Service
public class InquiryServiceImpl extends EntityValidator implements InquiryService {

    private final InquiryRepository inquiryRepository;

    public InquiryServiceImpl(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    @Override
    public InquiryDTO createInquiry(InquiryDTO inquiryDTO) {

        inquiryDTO.setBranchId(1l);
        validateEntity(inquiryDTO);

        final TRfInquiry tRfInquiry = InquiryMapper.INSTANCE.dtoToEntity(inquiryDTO);
        tRfInquiry.setInqrStatus(STATUS_ACTIVE.getShortValue());
        tRfInquiry.setInqrDateTime(LocalDateTime.now());

        return InquiryMapper.INSTANCE.entityToDTO(persistEntity(tRfInquiry));
    }

    @Override
    public InquiryDTO getInquiryById(Long inquiryId) {

        if(inquiryId == null)
            throw new NoRequiredInfoException("Inquiry Id is required");

        final TRfInquiry tRfInquiry = inquiryRepository.findByInqrIdAndInqrStatus(inquiryId, STATUS_ACTIVE.getShortValue());

        if(tRfInquiry == null)
            throw new DataNotFoundException("An Inquiry not not found for the id " + inquiryId);

        return InquiryMapper.INSTANCE.entityToDTO(tRfInquiry);
    }

    private TRfInquiry persistEntity(TRfInquiry tRfInquiry) {
        try {
            validateEntity(tRfInquiry);
            return inquiryRepository.save(tRfInquiry);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TransactionConflictException("Transaction Updated by Another User.");
        } catch (Exception e) {
            log.error("Error while persisting : " + e.getMessage());
            throw new OperationException(e.getMessage());
        }
    }
}
