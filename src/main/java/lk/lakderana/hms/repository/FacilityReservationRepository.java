package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TTrFacilityReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FacilityReservationRepository extends JpaRepository<TTrFacilityReservation, Long> {

    List<TTrFacilityReservation> findAllByReservation_ResvIdAndFareStatusAndBranch_BrnhIdIn(Long resvId, Short fareStatus,
                                                                                            List<Long> brnhIdList);
}
