package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsUserBranch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserBranchRepository extends JpaRepository<TMsUserBranch, String> {

    List<TMsUserBranch> findAllByUser_UserIdAndUsbrStatus(Long userId, Short usbrStatus);
}
