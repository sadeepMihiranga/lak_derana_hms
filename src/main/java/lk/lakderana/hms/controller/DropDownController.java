package lk.lakderana.hms.controller;

import lk.lakderana.hms.response.SuccessResponse;
import lk.lakderana.hms.response.SuccessResponseHandler;
import lk.lakderana.hms.service.DropDownService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/select/list")
public class DropDownController {

    private final DropDownService dropDownService;

    public DropDownController(DropDownService dropDownService) {
        this.dropDownService = dropDownService;
    }

    @GetMapping("/{code}")
    public ResponseEntity<SuccessResponse> getPartyByPartyId(@PathVariable("code") String code) throws IOException {
        return SuccessResponseHandler.generateResponse(dropDownService.getDropDownByCode(code));
    }
}
