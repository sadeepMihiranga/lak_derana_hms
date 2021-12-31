package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TRfBranch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BranchRepository extends JpaRepository<TRfBranch, Long> {

    TRfBranch findByBrnhIdAndBrnhStatus(Long brnhId, Short brnhStatus);

    List<TRfBranch> findAllByBrnhStatus(Short brnhStatus);
}
