package lk.lakderana.hms.controller;

import lk.lakderana.hms.dto.FacilityDTO;
import lk.lakderana.hms.response.SuccessResponse;
import lk.lakderana.hms.response.SuccessResponseHandler;
import lk.lakderana.hms.service.FacilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/facility")
public class FacilityController {

    private final FacilityService facilityService;

    public FacilityController(FacilityService facilityService) {
        this.facilityService = facilityService;
    }

    @GetMapping(path = "/search")
    public ResponseEntity<SuccessResponse> facilityPaginatedSearch(@RequestParam(name = "facilityType", required = false) String facilityType,
                                                                   @RequestParam(name = "facilityName", required = false) String facilityName,
                                                                   @RequestParam(name = "status", required = false) Short status,
                                                                   @RequestParam(name = "page", required = true) Integer page,
                                                                   @RequestParam(name = "size", required = true) Integer size) {
        return SuccessResponseHandler.generateResponse(facilityService
                .facilityPaginatedSearch(facilityName, facilityType, status, page, size));
    }


    @PostMapping
    public ResponseEntity<SuccessResponse> createFacility(@RequestBody FacilityDTO facilityDTO) {
        return SuccessResponseHandler.generateResponse(facilityService.createFacility(facilityDTO));
    }

    @PutMapping("/{facilityId}")
    public ResponseEntity<SuccessResponse> createFacility(@RequestBody FacilityDTO facilityDTO,
                                                          @PathVariable("facilityId") Long facilityId) {
        return SuccessResponseHandler.generateResponse(facilityService.updateFacility(facilityId, facilityDTO));
    }

    @DeleteMapping("/{facilityId}")
    public ResponseEntity<SuccessResponse> removeFacility(@PathVariable("facilityId") Long facilityId) {
        return SuccessResponseHandler.generateResponse(facilityService.removeFacility(facilityId));
    }

    @GetMapping("/{facilityId}")
    public ResponseEntity<SuccessResponse> getFacilityById(@PathVariable("facilityId") Long facilityId) {
        return SuccessResponseHandler.generateResponse(facilityService.getFacilityById(facilityId));
    }
}
