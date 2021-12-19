package lk.lakderana.hms.controller;

import lk.lakderana.hms.dto.InquiryDTO;
import lk.lakderana.hms.dto.UserDTO;
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
}
