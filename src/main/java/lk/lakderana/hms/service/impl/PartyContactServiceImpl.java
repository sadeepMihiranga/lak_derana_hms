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
                .findByPrtyCodeAndPrtyStatus(partyContactDTO.getPartyCode(), Constants.STATUS_ACTIVE.getShortValue());

        if(tMsParty == null)
            throw new DataNotFoundException("Party not found for the Code : " + partyContactDTO.getPartyCode());

        final TMsPartyContact tMsPartyContact = PartyContactMapper.INSTANCE.dtoToEntity(partyContactDTO);

        tMsPartyContact.setPtcnStatus(Constants.STATUS_ACTIVE.getShortValue());
        tMsPartyContact.setParty(tMsParty);

        final TMsPartyContact createdPartyContact = partyContactRepository.save(tMsPartyContact);

        return PartyContactMapper.INSTANCE.entityToDTO(createdPartyContact);
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

            final TMsParty tMsParty = partyRepository.findByPrtyCodeAndPrtyStatus(partyCode, Constants.STATUS_ACTIVE.getShortValue());

            if(tMsParty == null)
                throw new DataNotFoundException("Party not found for the Code : " + partyCode);
        }

        final List<TMsPartyContact> tMsPartyContactList = partyContactRepository
                .findAllByParty_PrtyCodeAndPtcnStatus(partyCode, Constants.STATUS_ACTIVE.getShortValue());

        List<PartyContactDTO> contactDTOList = new ArrayList<>();

        tMsPartyContactList.forEach(tMsPartyContact -> {
            contactDTOList.add(PartyContactMapper.INSTANCE.entityToDTO(tMsPartyContact));
        });

        return contactDTOList;
    }
}
