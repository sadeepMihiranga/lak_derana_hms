package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TRfBranch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<TRfBranch, String> {

    TRfBranch findByBrnhIdAndBrnhStatus(Long brnhId, Short brnhStatus);
}
