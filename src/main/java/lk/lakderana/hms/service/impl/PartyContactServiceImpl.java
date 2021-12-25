package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.dto.PartyContactDTO;
import lk.lakderana.hms.entity.TMsParty;
import lk.lakderana.hms.entity.TMsPartyContact;
import lk.lakderana.hms.entity.TRfInquiry;
import lk.lakderana.hms.exception.*;
import lk.lakderana.hms.mapper.PartyContactMapper;
import lk.lakderana.hms.repository.PartyContactRepository;
import lk.lakderana.hms.repository.PartyRepository;
import lk.lakderana.hms.service.PartyContactService;
import lk.lakderana.hms.util.Constants;
import lk.lakderana.hms.config.EntityValidator;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class PartyContactServiceImpl extends EntityValidator implements PartyContactService {

    private final PartyContactRepository partyContactRepository;
    private final PartyRepository partyRepository;

    public PartyContactServiceImpl(PartyContactRepository partyContactRepository,
                                   PartyRepository partyRepository) {
        this.partyContactRepository = partyContactRepository;
        this.partyRepository = partyRepository;
    }

    @Override
    public PartyContactDTO insertPartyContact(PartyContactDTO partyContactDTO) {

        TMsPartyContact tMsPartyContact = null;

        if(Strings.isNullOrEmpty(partyContactDTO.getContactNumber()))
            throw new NoRequiredInfoException("Contact Number is required");

        if(Strings.isNullOrEmpty(partyContactDTO.getPartyCode()))
            throw new NoRequiredInfoException("Party Code is required");

        final TMsParty tMsParty = partyRepository
                .findByPrtyCodeAndPrtyStatus(partyContactDTO.getPartyCode(), Constants.STATUS_ACTIVE.getShortValue());

        if(tMsParty == null)
            throw new DataNotFoundException("Party not found for the Code : " + partyContactDTO.getPartyCode());

        try {
            tMsPartyContact = PartyContactMapper.INSTANCE.dtoToEntity(partyContactDTO);

            tMsPartyContact.setPtcnStatus(Constants.STATUS_ACTIVE.getShortValue());
            tMsPartyContact.setParty(tMsParty);
        } catch (Exception e) {
            log.error("Error while creating a Party Contact {0} ", e.getMessage());
            throw new OperationException("Error while creating a Party Contact");
        }

        return PartyContactMapper.INSTANCE.entityToDTO(persistEntity(tMsPartyContact));
    }

    @Override
    public PartyContactDTO updatePartyContact(PartyContactDTO partyContactDTO, String partyCode, Long contactId) {
        return null;
    }

    @Override
    public List<PartyContactDTO> getContactsByPartyCode(String partyCode, Boolean isPartyValidated) {

        if(!isPartyValidated) {
            if(Strings.isNullOrEmpty(partyCode))
                throw new NoRequiredInfoException("Party Code is required");

            final TMsParty tMsParty = partyRepository
                    .findByPrtyCodeAndPrtyStatus(partyCode, Constants.STATUS_ACTIVE.getShortValue());

            if(tMsParty == null)
                throw new DataNotFoundException("Party not found for the Code : " + partyCode);
        }

        final List<TMsPartyContact> tMsPartyContactList = partyContactRepository
                .findAllByParty_PrtyCodeAndPtcnStatus(partyCode, Constants.STATUS_ACTIVE.getShortValue());

        if(tMsPartyContactList.isEmpty() || tMsPartyContactList == null)
            return Collections.emptyList();

        List<PartyContactDTO> contactDTOList = new ArrayList<>();

        tMsPartyContactList.forEach(tMsPartyContact -> {
            contactDTOList.add(PartyContactMapper.INSTANCE.entityToDTO(tMsPartyContact));
        });

        return contactDTOList;
    }

    @Override
    public PartyContactDTO getContactsByPartyCodeAndType(String partyCode, String contactType) {

        if(Strings.isNullOrEmpty(partyCode))
            throw new NoRequiredInfoException("Party Code is required");

        if(Strings.isNullOrEmpty(contactType))
            throw new NoRequiredInfoException("Contact Type Code is required");

        final TMsParty tMsParty = partyRepository
                .findByPrtyCodeAndPrtyStatus(partyCode, Constants.STATUS_ACTIVE.getShortValue());

        if(tMsParty == null)
            throw new DataNotFoundException("Party not found for the Code : " + partyCode);

        // TODO : this should be getting active type of contact for the given type
        final TMsPartyContact tMsPartyContact = partyContactRepository
                .findAllByParty_PrtyCodeAndPtcnContactTypeAndPtcnStatus(partyCode, contactType, Constants.STATUS_ACTIVE.getShortValue());

        if(tMsPartyContact == null)
            throw new DataNotFoundException("Contact Not found");

        return PartyContactMapper.INSTANCE.entityToDTO(tMsPartyContact);
    }

    @Override
    public Boolean removePartyContactByPartyCode(String partyCode) {

        if (Strings.isNullOrEmpty(partyCode))
            throw new InvalidDataException("Party Code is required");

        final TMsParty tMsParty = partyRepository
                .findByPrtyCodeAndPrtyStatus(partyCode, Constants.STATUS_ACTIVE.getShortValue());

        if(tMsParty == null)
            throw new DataNotFoundException("Party not found for the Code : " + partyCode);

        final List<TMsPartyContact> tMsPartyContactList = partyContactRepository
                .findAllByParty_PrtyCodeAndPtcnStatus(partyCode, Constants.STATUS_ACTIVE.getShortValue());

        if(tMsPartyContactList.isEmpty())
            throw new DataNotFoundException("Party Contacts not found for the Party Code : " + partyCode);

        tMsPartyContactList.forEach(tMsPartyContact -> {
            tMsPartyContact.setPtcnStatus(Constants.STATUS_INACTIVE.getShortValue());
            persistEntity(tMsPartyContact);
        });

        return true;
    }

    private TMsPartyContact persistEntity(TMsPartyContact tMsPartyContact) {
        try {
            validateEntity(tMsPartyContact);
            return partyContactRepository.save(tMsPartyContact);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TransactionConflictException("Transaction Updated by Another User.");
        } catch (Exception e) {
            log.error("Error while persisting : " + e.getMessage());
            throw new OperationException(e.getMessage());
        }
    }
}
