package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.PartyContactDTO;
import lk.lakderana.hms.dto.PartyDTO;
import lk.lakderana.hms.entity.TMsDepartment;
import lk.lakderana.hms.entity.TMsParty;
import lk.lakderana.hms.entity.TRfBranch;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.InvalidDataException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.PartyMapper;
import lk.lakderana.hms.repository.BranchRepository;
import lk.lakderana.hms.repository.DepartmentRepository;
import lk.lakderana.hms.repository.NumberGeneratorRepository;
import lk.lakderana.hms.repository.PartyRepository;
import lk.lakderana.hms.service.CommonReferenceService;
import lk.lakderana.hms.service.PartyContactService;
import lk.lakderana.hms.service.PartyService;
import lk.lakderana.hms.util.CommonReferenceTypeCodes;
import lk.lakderana.hms.util.Constants;
import lk.lakderana.hms.config.EntityValidator;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class PartyServiceImpl extends EntityValidator implements PartyService {

    private final CommonReferenceService commonReferenceService;
    private final PartyContactService partyContactService;

    private final PartyRepository partyRepository;
    private final DepartmentRepository departmentRepository;
    private final BranchRepository branchRepository;
    private final NumberGeneratorRepository numberGeneratorRepository;

    public PartyServiceImpl(PartyRepository partyRepository,
                            CommonReferenceService commonReferenceService,
                            PartyContactService partyContactService, DepartmentRepository departmentRepository,
                            BranchRepository branchRepository,
                            NumberGeneratorRepository numberGeneratorRepository) {
        this.partyRepository = partyRepository;
        this.commonReferenceService = commonReferenceService;
        this.partyContactService = partyContactService;
        this.departmentRepository = departmentRepository;
        this.branchRepository = branchRepository;
        this.numberGeneratorRepository = numberGeneratorRepository;
    }

    @Transactional
    @Override
    public PartyDTO createParty(PartyDTO partyDTO) {

        String partyNumber = null;
        validateEntity(partyDTO);

        final TMsParty tMsParty = PartyMapper.INSTANCE.dtoToEntity(partyDTO);

        populateAndValidatePartyReferenceDetails(tMsParty, partyDTO);

        partyDTO.setName(partyDTO.getFirstName() + " " + partyDTO.getLastName());

        try {
            partyNumber = numberGeneratorRepository.generateNumber("CU", "Y", "#", "#",
                    "#", "#", "#", "#");
        } catch (Exception e) {
            log.error("Error while creating a Party Code : " + e.getMessage());
            throw new OperationException("Error while creating a Party Code");
        }

        tMsParty.setPrtyStatus(Constants.STATUS_ACTIVE.getShortValue());
        tMsParty.setPrtyCode(partyNumber);

        final TMsParty createdParty = persistEntity(tMsParty);

        if(partyDTO.getContactList() != null) {
            partyDTO.getContactList().forEach(partyContactDTO -> {
                commonReferenceService
                        .getByCmrfCodeAndCmrtCode(CommonReferenceTypeCodes.PARTY_CONTACT_TYPES.getValue(), partyContactDTO.getContactType());

                partyContactDTO.setPartyCode(createdParty.getPrtyCode());

                partyContactService.insertPartyContact(partyContactDTO);
            });
        }

        return PartyMapper.INSTANCE.entityToDTO(createdParty);
    }

    @Override
    public PartyDTO getPartyByPartyCode(String partyCode) {

        if (Strings.isNullOrEmpty(partyCode))
            throw new InvalidDataException("Party Code is required");

        final TMsParty tMsParty = partyRepository.findByPrtyCodeAndPrtyStatus(partyCode, Constants.STATUS_ACTIVE.getShortValue());

        if(tMsParty == null)
            throw new DataNotFoundException("Party not found for the Code : " + partyCode);

        PartyDTO partyDTO = PartyMapper.INSTANCE.entityToDTO(tMsParty);

        final List<PartyContactDTO> contactDTOList = partyContactService.getContactsByPartyCode(partyDTO.getPartyCode(), true);
        partyDTO.setContactList(contactDTOList);

        return partyDTO;
    }

    @Transactional
    @Override
    public PartyDTO updateParty(String partyCode, PartyDTO partyDTO) {

        validateEntity(partyDTO);

        if (partyCode == null)
            throw new InvalidDataException("Party Code is required");

        final TMsParty tMsParty = partyRepository.findByPrtyCodeAndPrtyStatus(partyCode, Constants.STATUS_ACTIVE.getShortValue());

        if(tMsParty == null)
            throw new DataNotFoundException("Party not found for the Code : " + partyCode);

        populateAndValidatePartyReferenceDetails(tMsParty, partyDTO);

        partyDTO.setName(partyDTO.getFirstName() + " " + partyDTO.getLastName());
        tMsParty.setPrtyAddress1(partyDTO.getAddress1());
        tMsParty.setPrtyAddress2(partyDTO.getAddress2());
        tMsParty.setPrtyAddress3(partyDTO.getAddress3());
        tMsParty.setPrtyDob(partyDTO.getDob());
        tMsParty.setPrtyFirstName(partyDTO.getFirstName());
        tMsParty.setPrtyLastName(partyDTO.getLastName());
        tMsParty.setPrtyName(partyDTO.getName());
        tMsParty.setPrtyNic(partyDTO.getNic());
        tMsParty.setPrtyPassport(partyDTO.getPassport());

        tMsParty.setPrtyStatus(Constants.STATUS_ACTIVE.getShortValue());

        return PartyMapper.INSTANCE.entityToDTO(persistEntity(tMsParty));
    }

    private void populateAndValidatePartyReferenceDetails(TMsParty tMsParty, PartyDTO partyDTO) {

        commonReferenceService
                .getByCmrfCodeAndCmrtCode(CommonReferenceTypeCodes.PARTY_TYPES.getValue(), partyDTO.getType());

        tMsParty.setDepartment(null);
        if(!Strings.isNullOrEmpty(partyDTO.getDepartmentCode())) {
            final TMsDepartment tMsDepartment = departmentRepository
                    .findByDpmtCodeAndDpmtStatus(partyDTO.getDepartmentCode(), Constants.STATUS_ACTIVE.getShortValue());

            tMsParty.setDepartment(tMsDepartment);
        }

        tMsParty.setBranch(null);
        if(partyDTO.getBranchId() != null) {
            final TRfBranch tRfBranch = branchRepository
                    .findByBrnhIdAndBrnhStatus(partyDTO.getBranchId(), Constants.STATUS_ACTIVE.getShortValue());

            tMsParty.setBranch(tRfBranch);
        }
    }

    @Transactional
    @Override
    public Long removeParty(String partyCode) {
        return null;
    }

    @Override
    public PaginatedEntity partyPaginatedSearch(String name, String partyType, Integer page, Integer size) {

        PaginatedEntity paginatedPartyList = null;
        List<PartyDTO> customerList = null;

        if (page < 1)
            throw new InvalidDataException("Page should be a value greater than 0");

        if (size < 1)
            throw new InvalidDataException("Limit should be a value greater than 0");

        partyType = partyType.isEmpty() ? null : partyType;

        Page<TMsParty> tMsPartyPage = partyRepository
                .getActiveParties(name, Constants.STATUS_ACTIVE.getShortValue(), partyType, PageRequest.of(page - 1, size));

        if (tMsPartyPage.getSize() == 0)
            return null;

        paginatedPartyList = new PaginatedEntity();
        customerList = new ArrayList<>();

        for (TMsParty tMsParty : tMsPartyPage) {
            customerList.add(PartyMapper.INSTANCE.entityToDTO(tMsParty));
        }

        paginatedPartyList.setTotalNoOfPages(tMsPartyPage.getTotalPages());
        paginatedPartyList.setTotalNoOfRecords(tMsPartyPage.getTotalElements());
        paginatedPartyList.setEntities(customerList);

        return paginatedPartyList;
    }

    private TMsParty persistEntity(TMsParty tMsParty) {
        try {
            validateEntity(tMsParty);
            return partyRepository.save(tMsParty);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TransactionConflictException("Transaction Updated by Another User.");
        } catch (Exception e) {
            log.error("Error while persisting : " + e.getMessage());
            throw new OperationException(e.getMessage());
        }
    }
}
