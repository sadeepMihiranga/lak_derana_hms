package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TRfInquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<TRfInquiry, String> {

    TRfInquiry findByInqrIdAndInqrStatus(Long inqrId, Short inqrStatus);
}
