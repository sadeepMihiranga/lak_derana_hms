package lk.lakderana.hms.controller;

import lk.lakderana.hms.dto.AttendanceDTO;
import lk.lakderana.hms.response.SuccessResponse;
import lk.lakderana.hms.response.SuccessResponseHandler;
import lk.lakderana.hms.service.AttendanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@CrossOrigin
@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/mark")
    public ResponseEntity<SuccessResponse> markAttendance(@RequestBody AttendanceDTO attendanceDTO) {
        return SuccessResponseHandler.generateResponse(attendanceService.markAttendance(attendanceDTO));
    }

    @GetMapping(path = "/search")
    public ResponseEntity<SuccessResponse> attendancePaginatedSearch(@RequestParam(name = "customerCode", required = false) String customerCode,
                                                                    @RequestParam(name = "date", required = false) LocalDate date,
                                                                    @RequestParam(name = "status", required = false) Short status,
                                                                    @RequestParam(name = "page", required = true) Integer page,
                                                                    @RequestParam(name = "size", required = true) Integer size) {
        return SuccessResponseHandler.generateResponse(attendanceService
                .attendancePaginatedSearch(customerCode, date, status, page, size));
    }
}
