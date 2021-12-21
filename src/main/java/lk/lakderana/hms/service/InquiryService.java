package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.InquiryDTO;
import lk.lakderana.hms.dto.PaginatedEntity;

public interface InquiryService {

    InquiryDTO createInquiry(InquiryDTO inquiryDTO);

    InquiryDTO getInquiryById(Long inquiryId);

    PaginatedEntity inquiryPaginatedSearch(String customerName, String customerContactNo, String partyCode,
                                           Integer page, Integer size);
}
