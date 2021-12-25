package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.InquiryDTO;
import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.entity.TRfInquiry;
import lk.lakderana.hms.exception.*;
import lk.lakderana.hms.mapper.InquiryMapper;
import lk.lakderana.hms.repository.InquiryRepository;
import lk.lakderana.hms.service.InquiryService;
import lk.lakderana.hms.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static lk.lakderana.hms.util.Constants.STATUS_ACTIVE;

@Slf4j
@Service
public class InquiryServiceImpl extends EntityValidator implements InquiryService {

    private final HttpServletRequest request;

    private final InquiryRepository inquiryRepository;

    public InquiryServiceImpl(HttpServletRequest request,
                              InquiryRepository inquiryRepository) {
        this.request = request;
        this.inquiryRepository = inquiryRepository;
    }

    @Override
    public InquiryDTO createInquiry(InquiryDTO inquiryDTO) {

        InquiryDTO createdInquiry = null;

        inquiryDTO.setBranchId(captureBranchId());
        validateEntity(inquiryDTO);

        final TRfInquiry tRfInquiry = InquiryMapper.INSTANCE.dtoToEntity(inquiryDTO);
        tRfInquiry.setInqrStatus(STATUS_ACTIVE.getShortValue());
        tRfInquiry.setInqrDateTime(LocalDateTime.now());

        createdInquiry = InquiryMapper.INSTANCE.entityToDTO(persistEntity(tRfInquiry));

        return createdInquiry;
    }

    @Override
    public InquiryDTO getInquiryById(Long inquiryId) {

        final TRfInquiry tRfInquiry = validateInquiryById(inquiryId);

        return InquiryMapper.INSTANCE.entityToDTO(tRfInquiry);
    }

    private TRfInquiry validateInquiryById(Long inquiryId) {

        if(inquiryId == null)
            throw new NoRequiredInfoException("Inquiry Id is required");

        final TRfInquiry tRfInquiry = inquiryRepository.findByInqrIdAndInqrStatus(inquiryId, STATUS_ACTIVE.getShortValue());

        if(tRfInquiry == null)
            throw new DataNotFoundException("An Inquiry not not found for the id " + inquiryId);

        return tRfInquiry;
    }

    @Override
    public PaginatedEntity inquiryPaginatedSearch(String customerName, String customerContactNo, String partyCode,
                                                  Integer page, Integer size) {

        PaginatedEntity paginatedInquiryList = null;
        List<InquiryDTO> inquiryDTOList = null;

        if (page < 1)
            throw new InvalidDataException("Page should be a value greater than 0");

        if (size < 1)
            throw new InvalidDataException("Limit should be a value greater than 0");

        partyCode = partyCode.isEmpty() ? null : partyCode;

        final Page<TRfInquiry> tRfInquiryPage = inquiryRepository
                .getActiveInquiries(customerName, customerContactNo, partyCode,
                        STATUS_ACTIVE.getShortValue(), PageRequest.of(page - 1, size));

        if (tRfInquiryPage.getSize() == 0)
            return null;

        paginatedInquiryList = new PaginatedEntity();
        inquiryDTOList = new ArrayList<>();

        for (TRfInquiry tRfInquiry : tRfInquiryPage) {
            inquiryDTOList.add(InquiryMapper.INSTANCE.entityToDTO(tRfInquiry));
        }

        paginatedInquiryList.setTotalNoOfPages(tRfInquiryPage.getTotalPages());
        paginatedInquiryList.setTotalNoOfRecords(tRfInquiryPage.getTotalElements());
        paginatedInquiryList.setEntities(inquiryDTOList);

        return paginatedInquiryList;
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
