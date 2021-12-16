package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.PartyDTO;

import javax.transaction.Transactional;

public interface PartyService {

    @Transactional
    PartyDTO createParty(PartyDTO partyDTO);

    PartyDTO getPartyByPartyCode(String partyCode);

    @Transactional
    PartyDTO updateParty(String partyCode, PartyDTO partyDTO);

    @Transactional
    Long removeParty(String partyCode);

    PaginatedEntity partyPaginatedSearch(String name, String partyType, Integer page, Integer size);
}
