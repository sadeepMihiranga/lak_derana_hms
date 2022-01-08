package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsAttendance;
import lk.lakderana.hms.entity.TMsFacility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AttendanceRepository extends JpaRepository<TMsAttendance, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE TMsAttendance t SET t.attnOutTime = :outTime WHERE t.attnDate = :attendanceDate AND t.party.prtyCode = :employeeCode")
    Integer markOutTime(@Param("outTime") LocalTime outTime,
                        @Param("employeeCode") String employeeCode,
                        @Param("attendanceDate") LocalDate attendanceDate);

    TMsAttendance findByAttnDateAndParty_PrtyCodeAndAndAttnStatus(LocalDate attnDate, String prtyCode, Short attnStatus);

    @Query("SELECT t FROM TMsAttendance t " +
            "WHERE UPPER(t.party.prtyCode) LIKE CONCAT('%', UPPER(:employeeCode), '%') " +
            "AND (:date IS NULL OR (:status IS NOT NULL AND t.attnDate = :date)) " +
            "AND (:status IS NULL OR (:status IS NOT NULL AND t.attnStatus = :status)) " +
            "AND t.branch.brnhId IN :branchIdList " +
            "ORDER BY t.lastModDate DESC")
    Page<TMsAttendance> searchAtendance(@Param("employeeCode") String employeeCode,
                                        @Param("date") LocalDate date,
                                        @Param("status") Short status,
                                        @Param("branchIdList") List<Long> branchIdList,
                                        Pageable pageable);
}
