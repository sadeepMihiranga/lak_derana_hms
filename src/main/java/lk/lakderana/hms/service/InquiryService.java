package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.InquiryDTO;

public interface InquiryService {

    InquiryDTO createInquiry(InquiryDTO inquiryDTO);

    InquiryDTO getInquiryById(Long inquiryId);
}
