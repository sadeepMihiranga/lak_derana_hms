package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.FacilityDTO;
import lk.lakderana.hms.dto.FacilityReservationDTO;
import lk.lakderana.hms.entity.TMsFacility;
import lk.lakderana.hms.entity.TTrFacilityReservation;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.FacilityMapper;
import lk.lakderana.hms.repository.BranchRepository;
import lk.lakderana.hms.repository.FacilityRepository;
import lk.lakderana.hms.repository.FacilityReservationRepository;
import lk.lakderana.hms.repository.ReservationRepository;
import lk.lakderana.hms.service.FacilityReservationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import static lk.lakderana.hms.util.constant.Constants.STATUS_ACTIVE;

@Slf4j
@Service
public class FacilityReservationServiceImpl extends EntityValidator implements FacilityReservationService {

    private final FacilityReservationRepository facilityReservationRepository;
    private final FacilityRepository facilityRepository;
    private final ReservationRepository reservationRepository;
    private final BranchRepository branchRepository;

    public FacilityReservationServiceImpl(FacilityReservationRepository facilityReservationRepository,
                                          FacilityRepository facilityRepository,
                                          ReservationRepository reservationRepository,
                                          BranchRepository branchRepository) {
        this.facilityReservationRepository = facilityReservationRepository;
        this.facilityRepository = facilityRepository;
        this.reservationRepository = reservationRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public FacilityReservationDTO reserveFacility(Long reservationId, FacilityDTO facilityDTO) {

        if(facilityDTO.getFacilityId() == null)
            throw new NoRequiredInfoException("Facility Id is required");

        final TMsFacility tMsFacility = facilityRepository
                .findByFcltIdAndBranch_BrnhIdIn(facilityDTO.getFacilityId(), captureBranchIds());

        if(tMsFacility == null)
            throw new DataNotFoundException("Facility not found for the Id " + facilityDTO.getFacilityId());

        TTrFacilityReservation tTrFacilityReservation = new TTrFacilityReservation();

        tTrFacilityReservation.setFacility(tMsFacility);
        tTrFacilityReservation.setReservation(reservationRepository.findByResvIdAndBranch_BrnhIdIn(reservationId, captureBranchIds()));
        tTrFacilityReservation.setBranch(branchRepository.getById(captureBranchIds().get(0)));
        tTrFacilityReservation.setFareStatus(STATUS_ACTIVE.getShortValue());
        tTrFacilityReservation.setFareQuantity(facilityDTO.getQuantity().doubleValue());

        final TTrFacilityReservation createdFacilityReservation = persistEntity(tTrFacilityReservation);

        return new FacilityReservationDTO(
                createdFacilityReservation.getFareId(),
                FacilityMapper.INSTANCE.entityToDTO(tMsFacility),
                createdFacilityReservation.getFareStatus());
    }

    private TTrFacilityReservation persistEntity(TTrFacilityReservation tTrFacilityReservation) {
        try {
            validateEntity(tTrFacilityReservation);
            return facilityReservationRepository.save(tTrFacilityReservation);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TransactionConflictException("Transaction Updated by Another User.");
        } catch (Exception e) {
            log.error("Error while persisting : " + e.getMessage());
            throw new OperationException(e.getMessage());
        }
    }
}
