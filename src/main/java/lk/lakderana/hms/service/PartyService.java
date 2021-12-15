package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.PartyDTO;

import javax.transaction.Transactional;

public interface PartyService {

    @Transactional
    PartyDTO createParty(PartyDTO partyDTO);

    PartyDTO getPartyByPartyId(Long partyId);

    @Transactional
    PartyDTO updateParty(Long partyId, PartyDTO partyDTO);

    @Transactional
    Long removeParty(Long partyId);

    PaginatedEntity partyPaginatedSearch(String name, String partyType, Integer page, Integer size);
}
