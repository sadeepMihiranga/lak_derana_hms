package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.dto.PartyDTO;
import lk.lakderana.hms.entity.TMsParty;
import lk.lakderana.hms.exception.InvalidDataException;
import lk.lakderana.hms.mapper.PartyMapper;
import lk.lakderana.hms.repository.PartyRepository;
import lk.lakderana.hms.service.PartyService;
import lk.lakderana.hms.util.Constants;
import lk.lakderana.hms.util.EntityValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class PartyServiceImpl extends EntityValidator implements PartyService {

    private final PartyRepository partyRepository;

    public PartyServiceImpl(PartyRepository partyRepository) {
        this.partyRepository = partyRepository;
    }

    @Override
    public PartyDTO createParty(PartyDTO partyDTO) {
        return null;
    }

    @Override
    public PartyDTO getPartyByPartyId(Long partyId) {
        return null;
    }

    @Override
    public PartyDTO updateParty(Long partyId, PartyDTO partyDTO) {
        return null;
    }

    @Override
    public Long removeParty(Long partyId) {
        return null;
    }

    @Override
    public PaginatedEntity partyPaginatedSearch(String name, String partyType, Integer page, Integer size) {

        PaginatedEntity paginatedPartyList = null;
        List<PartyDTO> customerList = null;

        if (page < 1)
            throw new InvalidDataException("Page should be a value greater than 0");

        if (size < 1)
            throw new InvalidDataException("Limit should be a value greater than 0");

        partyType = partyType.isEmpty() ? null : partyType;

        Page<TMsParty> tMsPartyPage = partyRepository
                .getActiveParties(name, Constants.STATUS_ACTIVE.getShortValue(), partyType, PageRequest.of(page - 1, size));

        if (tMsPartyPage.getSize() == 0)
            return null;

        paginatedPartyList = new PaginatedEntity();
        customerList = new ArrayList<>();

        for (TMsParty tMsParty : tMsPartyPage) {
            customerList.add(PartyMapper.INSTANCE.entityToDTO(tMsParty));
        }

        paginatedPartyList.setTotalNoOfPages(tMsPartyPage.getTotalPages());
        paginatedPartyList.setTotalNoOfRecords(tMsPartyPage.getTotalElements());
        paginatedPartyList.setEntities(Collections.singletonList(customerList));

        return paginatedPartyList;
    }
}
