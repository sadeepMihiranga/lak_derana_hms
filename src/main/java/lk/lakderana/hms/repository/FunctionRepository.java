package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsFunction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunctionRepository extends JpaRepository<TMsFunction, String> {
}
