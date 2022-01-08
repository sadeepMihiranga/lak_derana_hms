package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.AttendanceDTO;
import lk.lakderana.hms.dto.PaginatedEntity;

import java.time.LocalDate;

public interface AttendanceService {

    Boolean markAttendance(AttendanceDTO attendanceDTO);

    PaginatedEntity attendancePaginatedSearch(String employeeCode, LocalDate date, Short status, Integer page, Integer size);
}
