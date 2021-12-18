package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<TMsUserRole, Long> {

    List<TMsUserRole> findAllByUser_UserIdAndUsrlStatus(Long userId, Short usrlStatus);

    TMsUserRole findByUser_UserIdAndRole_RoleIdAndUsrlStatus(Long userId, Long roleId, Short usrlStatus);
}
