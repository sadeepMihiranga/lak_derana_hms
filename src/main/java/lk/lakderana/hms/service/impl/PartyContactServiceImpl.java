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
import lk.lakderana.hms.util.EntityValidator;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Strings;
import org.springframework.stereotype.Service;

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

        final TMsParty tMsParty = partyRepository.findById(partyContactDTO.getPartyId())
                .orElseThrow(() -> new DataNotFoundException("Party not found Id : " + partyContactDTO.getPartyId()));

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
    public PartyContactDTO getContactByPartyId(Long partyId) {
        return null;
    }
}
