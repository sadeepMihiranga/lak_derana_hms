package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.AttendanceDTO;
import lk.lakderana.hms.dto.FacilityDTO;
import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.entity.TMsAttendance;
import lk.lakderana.hms.entity.TMsFacility;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.AttendanceMapper;
import lk.lakderana.hms.mapper.FacilityMapper;
import lk.lakderana.hms.repository.AttendanceRepository;
import lk.lakderana.hms.service.AttendanceService;
import lk.lakderana.hms.service.PartyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static lk.lakderana.hms.util.constant.Constants.STATUS_ACTIVE;

@Slf4j
@Service
public class AttendanceServiceImpl extends EntityValidator implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    private final PartyService partyService;

    public AttendanceServiceImpl(AttendanceRepository attendanceRepository, PartyService partyService) {
        this.attendanceRepository = attendanceRepository;
        this.partyService = partyService;
    }

    @Override
    public Boolean markAttendance(AttendanceDTO attendanceDTO) {

        validateEntity(attendanceDTO);

        if(attendanceDTO.getAttendanceDate().isAfter(LocalDate.now()))
            throw new OperationException("Date cannot be future date");

        partyService.getPartyByPartyCode(attendanceDTO.getEmployeeCode());

        if(attendanceDTO.getOutTime() != null && attendanceDTO.getInTime() == null) {
            final Integer marked = attendanceRepository.markOutTime(attendanceDTO.getOutTime(), attendanceDTO.getEmployeeCode(), attendanceDTO.getAttendanceDate());

            if(marked <= 0)
                throw new OperationException("Out Time not recorded correctly");
        } else if(attendanceDTO.getOutTime() == null && attendanceDTO.getInTime() != null) {
            attendanceDTO.setBranchId(captureBranchIds().get(0));
            attendanceDTO.setStatus(STATUS_ACTIVE.getShortValue());
            persistEntity(AttendanceMapper.INSTANCE.dtoToEntity(attendanceDTO));
        } else if(attendanceDTO.getOutTime() != null && attendanceDTO.getInTime() != null)  {

            final TMsAttendance tMsAttendance = attendanceRepository
                    .findByAttnDateAndParty_PrtyCodeAndAndAttnStatus(attendanceDTO.getAttendanceDate(), attendanceDTO.getEmployeeCode(), STATUS_ACTIVE.getShortValue());

            if(tMsAttendance != null)
                throw new OperationException("Attendance marked already. Date : " + attendanceDTO.getAttendanceDate());

            attendanceDTO.setBranchId(captureBranchIds().get(0));
            attendanceDTO.setStatus(STATUS_ACTIVE.getShortValue());
            persistEntity(AttendanceMapper.INSTANCE.dtoToEntity(attendanceDTO));
        }

        return true;
    }

    @Override
    public PaginatedEntity attendancePaginatedSearch(String employeeCode, LocalDate date, Short status, Integer page, Integer size) {

        PaginatedEntity paginatedAttendanceList = null;
        List<AttendanceDTO> attendanceDTOList = null;

        validatePaginateIndexes(page, size);

        final Page<TMsAttendance> tMsAttendances = attendanceRepository
                .searchAtendance(employeeCode, date, status, captureBranchIds(), PageRequest.of(page - 1, size));

        if (tMsAttendances.getSize() == 0)
            return null;

        paginatedAttendanceList = new PaginatedEntity();
        attendanceDTOList = new ArrayList<>();

        for (TMsAttendance tMsAttendance : tMsAttendances) {
            attendanceDTOList.add(AttendanceMapper.INSTANCE.entityToDTO(tMsAttendance));
        }

        paginatedAttendanceList.setTotalNoOfPages(tMsAttendances.getTotalPages());
        paginatedAttendanceList.setTotalNoOfRecords(tMsAttendances.getTotalElements());
        paginatedAttendanceList.setEntities(attendanceDTOList);

        return paginatedAttendanceList;
    }

    private TMsAttendance persistEntity(TMsAttendance tMsAttendance) {
        try {
            validateEntity(tMsAttendance);
            return attendanceRepository.save(tMsAttendance);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TransactionConflictException("Transaction Updated by Another User.");
        } catch (Exception e) {
            log.error("Error while persisting : " + e.getMessage());
            throw new OperationException(e.getMessage());
        }
    }
}
