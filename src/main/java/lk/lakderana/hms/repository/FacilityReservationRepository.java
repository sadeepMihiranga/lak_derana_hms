package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TTrFacilityReservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityReservationRepository extends JpaRepository<TTrFacilityReservation, Long> {
}
