package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.InquiryDTO;
import lk.lakderana.hms.dto.PaginatedEntity;

public interface InquiryService {

    InquiryDTO createInquiry(InquiryDTO inquiryDTO);

    InquiryDTO getInquiryById(Long inquiryId);

    PaginatedEntity inquiryPaginatedSearch(String customerName, String customerContactNo, String partyCode,
                                           Short status, Integer page, Integer size);
    /**
     * @param         inquiryDTO InquiryDTO - that contains - which inquiry to transfer , where to transfer
     * @implNote      Transfer an inquiry to another branch in the hotel chain
     * @return        New inquiry created on the accepted branch
     */
    InquiryDTO transferInquiry(InquiryDTO inquiryDTO);

    Boolean cancelInquiryById(Long inquiryId);

    Boolean reserveInquiryById(Long inquiryId);

    InquiryDTO updateInquiry(Long inquiryId, InquiryDTO inquiryDTO);
}
