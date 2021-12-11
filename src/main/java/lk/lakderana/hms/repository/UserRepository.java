package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<TMsUser, Long> {

    TMsUser findByUserUsername(String userUsername);
}
