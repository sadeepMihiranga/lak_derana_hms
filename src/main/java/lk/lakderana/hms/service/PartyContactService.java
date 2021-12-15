package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.PartyContactDTO;

import java.util.List;

public interface PartyContactService {

    PartyContactDTO insertPartyContact(PartyContactDTO partyContactDTO);

    PartyContactDTO updatePartyContact(PartyContactDTO partyContactDTO, Long partyId, Long contactId);

    List<PartyContactDTO> getContactsByPartyId(Long partyId, Boolean isPartyValidated);
}
