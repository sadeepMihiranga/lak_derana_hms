package lk.lakderana.hms.controller;

import lk.lakderana.hms.response.SuccessResponse;
import lk.lakderana.hms.response.SuccessResponseHandler;
import lk.lakderana.hms.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse> getPaginatedPayments(@RequestParam(name = "paymentMethod", required = false) String paymentMethod,
                                                                @RequestParam(name = "reservationId", required = false) Long reservationId,
                                                                @RequestParam(name = "status", required = false) Short status,
                                                                @RequestParam(name = "page", required = true) Integer page,
                                                                @RequestParam(name = "size", required = true) Integer size) {
        return SuccessResponseHandler.generateResponse(paymentService.paymentPaginatedSearch(paymentMethod, reservationId, status, page, size));
    }
}
