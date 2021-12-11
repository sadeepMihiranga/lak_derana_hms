package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<TMsUserRole, Long> {

    @Query("SELECT t FROM TMsUserRole t WHERE t.user.userId = :id")
    List<TMsUserRole> findAllById(@Param("id") Long id);
}
