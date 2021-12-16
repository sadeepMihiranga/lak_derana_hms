package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.dto.PartyContactDTO;
import lk.lakderana.hms.entity.TMsParty;
import lk.lakderana.hms.entity.TMsPartyContact;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.mapper.PartyContactMapper;
import lk.lakderana.hms.repository.PartyContactRepository;
import lk.lakderana.hms.repository.PartyRepository;
import lk.lakderana.hms.service.PartyContactService;
import lk.lakderana.hms.util.Constants;
import lk.lakderana.hms.config.EntityValidator;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

        if(Strings.isNullOrEmpty(partyContactDTO.getContactNumber()))
            throw new NoRequiredInfoException("Contact Number is required");

        final TMsParty tMsParty = partyRepository
                .findByPrtyIdAndPrtyStatus(partyContactDTO.getPartyId(), Constants.STATUS_ACTIVE.getShortValue());

        if(tMsParty == null)
            throw new DataNotFoundException("Party not found for the ID : " + partyContactDTO.getPartyId());

        final TMsPartyContact tMsPartyContact = PartyContactMapper.INSTANCE.dtoToEntity(partyContactDTO);

        tMsPartyContact.setPtcnStatus(Constants.STATUS_ACTIVE.getShortValue());
        tMsPartyContact.setParty(tMsParty);

        final TMsPartyContact createdPartyContact = partyContactRepository.save(tMsPartyContact);

        return PartyContactMapper.INSTANCE.entityToDTO(createdPartyContact);
    }

    @Override
    public PartyContactDTO updatePartyContact(PartyContactDTO partyContactDTO, Long partyId, Long contactId) {
        return null;
    }

    @Override
    public List<PartyContactDTO> getContactsByPartyId(Long partyId, Boolean isPartyValidated) {

        if(!isPartyValidated) {
            if(partyId == null)
                throw new NoRequiredInfoException("Party Id is required");

            final TMsParty tMsParty = partyRepository.findByPrtyIdAndPrtyStatus(partyId, Constants.STATUS_ACTIVE.getShortValue());

            if(tMsParty == null)
                throw new DataNotFoundException("Party not found for the Id : " + partyId);
        }

        final List<TMsPartyContact> tMsPartyContactList = partyContactRepository
                .findAllByParty_PrtyIdAndPtcnStatus(partyId, Constants.STATUS_ACTIVE.getShortValue());

        List<PartyContactDTO> contactDTOList = new ArrayList<>();

        tMsPartyContactList.forEach(tMsPartyContact -> {
            contactDTOList.add(PartyContactMapper.INSTANCE.entityToDTO(tMsPartyContact));
        });

        return contactDTOList;
    }
}
