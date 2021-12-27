package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsFacility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacilityRepository extends JpaRepository<TMsFacility, Long> {
}
