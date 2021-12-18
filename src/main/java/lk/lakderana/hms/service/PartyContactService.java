package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.PartyContactDTO;

import java.util.List;

public interface PartyContactService {

    PartyContactDTO insertPartyContact(PartyContactDTO partyContactDTO);

    PartyContactDTO updatePartyContact(PartyContactDTO partyContactDTO, String partyCode, Long contactId);

    List<PartyContactDTO> getContactsByPartyCode(String partyCode, Boolean isPartyValidated);

    PartyContactDTO getContactsByPartyCodeAndType(String partyCode, String contactType);
}
