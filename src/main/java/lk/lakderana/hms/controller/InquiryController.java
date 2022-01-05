package lk.lakderana.hms.controller;

import lk.lakderana.hms.dto.InquiryDTO;
import lk.lakderana.hms.response.SuccessResponse;
import lk.lakderana.hms.response.SuccessResponseHandler;
import lk.lakderana.hms.service.InquiryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/inquiry")
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> createInquiry(@RequestBody InquiryDTO inquiryDTO) {
        return SuccessResponseHandler.generateResponse(inquiryService.createInquiry(inquiryDTO));
    }

    @GetMapping("/{inquiryId}")
    public ResponseEntity<SuccessResponse> getInquiryById(@PathVariable("inquiryId") Long inquiryId) {
        return SuccessResponseHandler.generateResponse(inquiryService.getInquiryById(inquiryId));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<SuccessResponse> customerPaginatedSearch(@RequestParam(name = "customerName", required = false) String customerName,
                                                                   @RequestParam(name = "customerContactNo", required = false) String customerContactNo,
                                                                   @RequestParam(name = "partyCode", required = false) String partyCode,
                                                                   @RequestParam(name = "status", required = false) Short status,
                                                                   @RequestParam(name = "page", required = true) Integer page,
                                                                   @RequestParam(name = "size", required = true) Integer size) {
        return SuccessResponseHandler.generateResponse(inquiryService
                .inquiryPaginatedSearch(customerName, customerContactNo, partyCode, status, page, size));
    }

    @PostMapping("/transfer")
    public ResponseEntity<SuccessResponse> transferInquiry(@RequestBody InquiryDTO inquiryDTO) {
        return SuccessResponseHandler.generateResponse(inquiryService.transferInquiry(inquiryDTO));
    }

    @PutMapping("/{inquiryId}/cancel")
    public ResponseEntity<SuccessResponse> cancelInquiry(@PathVariable("inquiryId") Long inquiryId) {
        return SuccessResponseHandler.generateResponse(inquiryService.cancelInquiryById(inquiryId));
    }

    @PutMapping("/{inquiryId}")
    public ResponseEntity<SuccessResponse> updateInquiry(@PathVariable("inquiryId") Long inquiryId,
                                                         @RequestBody InquiryDTO inquiryDTO) {
        return SuccessResponseHandler.generateResponse(inquiryService.updateInquiry(inquiryId, inquiryDTO));
    }
}
