package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.Role;
import lk.lakderana.hms.entity.RoleToUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<RoleToUser, Long> {

    @Query("SELECT t FROM RoleToUser t WHERE t.user.id = :id")
    List<RoleToUser> findAllById(@Param("id") Long id);
}
