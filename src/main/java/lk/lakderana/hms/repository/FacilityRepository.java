package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsFacility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FacilityRepository extends JpaRepository<TMsFacility, Long> {

    @Query("SELECT t FROM TMsFacility t " +
            "WHERE UPPER(t.fcltType) LIKE CONCAT('%', UPPER(:fcltType), '%') " +
            "AND UPPER(t.fcltName) LIKE CONCAT('%', UPPER(:fcltName), '%') " +
            "AND t.branch.brnhId IN :branchIdList " +
            "ORDER BY t.lastModUserCode")
    Page<TMsFacility> searchFacility(@Param("fcltName") String fcltName,
                                     @Param("fcltType") String fcltType,
                                     @Param("branchIdList") List<Long> branchIdList,
                                     Pageable pageable);

    TMsFacility findByFcltIdAndBranch_BrnhIdIn(Long fcltId, List<Long> brnhIdList);

    List<TMsFacility> findAllByFcltStatusAndBranch_BrnhIdIn(Short facltStatus, List<Long> brnhIdList);
}
