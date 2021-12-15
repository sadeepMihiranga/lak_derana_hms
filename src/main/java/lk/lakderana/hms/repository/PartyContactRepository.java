package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsPartyContact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartyContactRepository extends JpaRepository<TMsPartyContact, Long> {

    List<TMsPartyContact> findAllByParty_PrtyIdAndPtcnStatus(Long prtyId, Short ptcnStatus);
}
