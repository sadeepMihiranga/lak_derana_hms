package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.PartyDTO;

public interface PartyService {

    PartyDTO createParty(PartyDTO partyDTO);

    PartyDTO getPartyByPartyId(Long partyId);

    PartyDTO updateParty(Long partyId, PartyDTO partyDTO);

    Long removeParty(Long partyId);

    PaginatedEntity partyPaginatedSearch(String name, String partyType, Integer page, Integer size);
}
