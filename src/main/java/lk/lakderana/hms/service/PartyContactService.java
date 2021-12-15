package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.PartyContactDTO;

public interface PartyContactService {

    PartyContactDTO insertPartyContact(PartyContactDTO partyContactDTO);

    PartyContactDTO updatePartyContact(PartyContactDTO partyContactDTO, Long partyId, Long contactId);

    PartyContactDTO getContactByPartyId(Long partyId);
}
