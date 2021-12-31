package lk.lakderana.hms.controller;

import lk.lakderana.hms.dto.ReservationDTO;
import lk.lakderana.hms.response.SuccessResponse;
import lk.lakderana.hms.response.SuccessResponseHandler;
import lk.lakderana.hms.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse> getPaginatedUsers(@RequestParam(name = "noOfPersons", required = false) Integer noOfPersons,
                                                             @RequestParam(name = "status", required = false) Short status,
                                                             @RequestParam(name = "page", required = true) Integer page,
                                                             @RequestParam(name = "size", required = true) Integer size) {
        return SuccessResponseHandler.generateResponse(reservationService.reservationPaginatedSearch(status, noOfPersons, page, size));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> createReservation(@RequestBody ReservationDTO reservationDTO) {
        return SuccessResponseHandler.generateResponse(reservationService.createReservation(reservationDTO));
    }

    @PutMapping("/{reservationId}/cancel")
    public ResponseEntity<SuccessResponse> cancelReservation(@PathVariable("reservationId") Long reservationId,
                                                             @RequestBody ReservationDTO reservationDTO) {
        return SuccessResponseHandler.generateResponse(reservationService.cancelReservation(reservationId, reservationDTO));
    }
}
