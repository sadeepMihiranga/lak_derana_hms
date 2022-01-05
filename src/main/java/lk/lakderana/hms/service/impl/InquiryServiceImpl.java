package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.InquiryDTO;
import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.PartyDTO;
import lk.lakderana.hms.entity.TRfBranch;
import lk.lakderana.hms.entity.TRfInquiry;
import lk.lakderana.hms.exception.*;
import lk.lakderana.hms.mapper.InquiryMapper;
import lk.lakderana.hms.repository.BranchRepository;
import lk.lakderana.hms.repository.InquiryRepository;
import lk.lakderana.hms.service.InquiryService;
import lk.lakderana.hms.service.PartyService;
import lk.lakderana.hms.util.constant.status.InquiryStatus;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static lk.lakderana.hms.util.constant.CommonReferenceCodes.PARTY_CONTACT_MOBILE;
import static lk.lakderana.hms.util.constant.Constants.STATUS_ACTIVE;
import static lk.lakderana.hms.util.constant.status.InquiryStatus.*;

@Slf4j
@Service
public class InquiryServiceImpl extends EntityValidator implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final BranchRepository branchRepository;

    private final PartyService partyService;

    public InquiryServiceImpl(InquiryRepository inquiryRepository,
                              BranchRepository branchRepository,
                              PartyService partyService) {
        this.inquiryRepository = inquiryRepository;
        this.branchRepository = branchRepository;
        this.partyService = partyService;
    }

    @Override
    public InquiryDTO createInquiry(InquiryDTO inquiryDTO) {

        InquiryDTO createdInquiry = null;

        inquiryDTO.setBranchId(captureBranchIds().get(0));
        validateEntity(inquiryDTO);

        final TRfInquiry tRfInquiry = InquiryMapper.INSTANCE.dtoToEntity(inquiryDTO);
        tRfInquiry.setInqrStatus(STATUS_ACTIVE.getShortValue());
        tRfInquiry.setInqrDateTime(LocalDateTime.now());

        createdInquiry = InquiryMapper.INSTANCE.entityToDTO(persistEntity(tRfInquiry));

        return createdInquiry;
    }

    @Override
    public InquiryDTO getInquiryById(Long inquiryId) {

        if(inquiryId == null)
            throw new NoRequiredInfoException("Inquiry Id is required");

        final TRfInquiry tRfInquiry = inquiryRepository.findByInqrIdAndBranch_BrnhIdIn(inquiryId, captureBranchIds());

        if(tRfInquiry == null)
            throw new DataNotFoundException("An Inquiry not found for the id " + inquiryId);

        return InquiryMapper.INSTANCE.entityToDTO(tRfInquiry);
    }

    @Override
    public PaginatedEntity inquiryPaginatedSearch(String customerName, String customerContactNo, String partyCode,
                                                  Short status, Integer page, Integer size) {

        PaginatedEntity paginatedInquiryList = null;
        List<InquiryDTO> inquiryDTOList = null;

        validatePaginateIndexes(page, size);

        partyCode = partyCode.isEmpty() ? null : partyCode;

        final Page<TRfInquiry> tRfInquiryPage = inquiryRepository
                .getActiveInquiries(customerName, customerContactNo, partyCode, status, captureBranchIds(),
                        PageRequest.of(page - 1, size));

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

    /**
     * @param         inquiryDTO InquiryDTO - that contains - which inquiry to transfer , where to transfer
     * @implNote      Transfer an inquiry to another branch in the hotel chain
     * @return        New inquiry created on the accepted branch
     */
    @Override
    public InquiryDTO transferInquiry(InquiryDTO inquiryDTO) {

        TRfInquiry tRfInquiry = validateInquiryById(inquiryDTO.getInquiryId(), captureBranchIds());

        final TRfBranch tRfBranch = branchRepository
                .findByBrnhIdAndBrnhStatus(inquiryDTO.getBranchId(), STATUS_ACTIVE.getShortValue());

        if(tRfBranch == null)
            throw new DataNotFoundException("Branch Not found for Id " + inquiryDTO.getBranchId());

        tRfInquiry.setInqrStatus(TRANSFERRED_TO_ANOTHER.getShortValue());
        tRfInquiry.setInqrTransferredTo(inquiryDTO.getBranchId());
        inquiryRepository.save(tRfInquiry);

        InquiryDTO inquiryToTransfer = new InquiryDTO();

        inquiryToTransfer.setInquiryStatus(InquiryStatus.CREATED.getShortValue());
        inquiryToTransfer.setInquiryDateTime(LocalDateTime.now());
        inquiryToTransfer.setBranchId(inquiryDTO.getBranchId());
        inquiryToTransfer.setTransferredFrom(captureBranchIds().get(0));
        inquiryToTransfer.setCustomerName(tRfInquiry.getInqrCustomerName());
        inquiryToTransfer.setRemarks(tRfInquiry.getInqrRemarks());
        inquiryToTransfer.setCustomerContactNo(tRfInquiry.getInqrCustomerContactNo());
        inquiryToTransfer.setPartyCode(tRfInquiry.getInqrCustomerCode());

        final TRfInquiry transferredInquiry = inquiryRepository
                .save(InquiryMapper.INSTANCE.dtoToEntity(inquiryToTransfer));

        return InquiryMapper.INSTANCE.entityToDTO(transferredInquiry);
    }

    @Override
    public Boolean cancelInquiryById(Long inquiryId) {

        final TRfInquiry tRfInquiry = validateInquiryById(inquiryId, captureBranchIds());

        tRfInquiry.setInqrStatus(CANCELED.getShortValue());

        persistEntity(tRfInquiry);

        return true;
    }

    @Override
    public Boolean reserveInquiryById(Long inquiryId) {

        final TRfInquiry tRfInquiry = validateInquiryById(inquiryId, captureBranchIds());

        tRfInquiry.setInqrStatus(RESERVED.getShortValue());

        persistEntity(tRfInquiry);

        return true;
    }

    @Override
    public InquiryDTO updateInquiry(Long inquiryId, InquiryDTO inquiryDTO) {

        final TRfInquiry tRfInquiry = validateInquiryById(inquiryId, captureBranchIds());

        if(!Strings.isNullOrEmpty(inquiryDTO.getPartyCode())) {

            final PartyDTO existingCustomer = partyService.getPartyByPartyCode(inquiryDTO.getPartyCode());
            tRfInquiry.setInqrCustomerCode(existingCustomer.getPartyCode());
            tRfInquiry.setInqrCustomerName(existingCustomer.getName());

            final List<String> mobileNoList = existingCustomer.getContactList()
                    .stream()
                    .filter(partyContactDTO -> partyContactDTO.getContactType().equals(PARTY_CONTACT_MOBILE.getValue()))
                    .map(partyContactDTO -> partyContactDTO.getContactNumber())
                    .collect(Collectors.toList());

            if(!mobileNoList.isEmpty())
                tRfInquiry.setInqrCustomerContactNo(mobileNoList.get(0));
            else
                tRfInquiry.setInqrCustomerContactNo(inquiryDTO.getCustomerContactNo());

        } else {

            tRfInquiry.setInqrCustomerName(inquiryDTO.getCustomerName());
            tRfInquiry.setInqrCustomerContactNo(inquiryDTO.getCustomerContactNo());
        }

        tRfInquiry.setInqrRemarks(inquiryDTO.getRemarks());

        return InquiryMapper.INSTANCE.entityToDTO(persistEntity(tRfInquiry));
    }

    private TRfInquiry validateInquiryById(Long inquiryId, List<Long> branchIdList) {

        if(inquiryId == null)
            throw new NoRequiredInfoException("Inquiry Id is required");

        final TRfInquiry tRfInquiry = inquiryRepository
                .findByInqrIdAndBranch_BrnhIdInAndInqrStatus(inquiryId, branchIdList, STATUS_ACTIVE.getShortValue());

        if(tRfInquiry == null)
            throw new DataNotFoundException("An Inquiry not found for the id " + inquiryId);

        return tRfInquiry;
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
