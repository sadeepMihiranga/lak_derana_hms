package lk.lakderana.hms.service.impl;

import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.ItemDTO;
import lk.lakderana.hms.dto.ItemReservationDTO;
import lk.lakderana.hms.entity.TMsItem;
import lk.lakderana.hms.entity.TTrItemReservation;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.ItemMapper;
import lk.lakderana.hms.repository.BranchRepository;
import lk.lakderana.hms.repository.ItemRepository;
import lk.lakderana.hms.repository.ItemReservationRepository;
import lk.lakderana.hms.repository.ReservationRepository;
import lk.lakderana.hms.service.ItemReservationService;
import lk.lakderana.hms.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static lk.lakderana.hms.util.constant.Constants.STATUS_ACTIVE;
import static lk.lakderana.hms.util.constant.Constants.STATUS_INACTIVE;

@Slf4j
@Service
public class ItemReservationServiceImpl extends EntityValidator implements ItemReservationService {

    private final ItemRepository itemRepository;
    private final ItemReservationRepository itemReservationRepository;
    private final ReservationRepository reservationRepository;
    private final BranchRepository branchRepository;

    private final ItemService itemService;

    public ItemReservationServiceImpl(ItemRepository itemRepository,
                                      ItemReservationRepository itemReservationRepository,
                                      ReservationRepository reservationRepository,
                                      BranchRepository branchRepository,
                                      ItemService itemService) {
        this.itemRepository = itemRepository;
        this.itemReservationRepository = itemReservationRepository;
        this.reservationRepository = reservationRepository;
        this.branchRepository = branchRepository;
        this.itemService = itemService;
    }

    @Override
    public ItemReservationDTO reserveItem(Long reservationId, ItemDTO itemDTO) {

        final TMsItem tMsItem = validateItemById(itemDTO.getItemId());

        TTrItemReservation tTrItemReservation = new TTrItemReservation();

        tTrItemReservation.setReservation(reservationRepository.findByResvIdAndBranch_BrnhIdIn(reservationId, captureBranchIds()));
        tTrItemReservation.setItem(tMsItem);
        tTrItemReservation.setBranch(branchRepository.getById(captureBranchIds().get(0)));
        tTrItemReservation.setItrsQuantity(itemDTO.getQuantity().doubleValue());
        tTrItemReservation.setItrsStatus(STATUS_ACTIVE.getShortValue());

        final TTrItemReservation createdTTrItemReservation = persistEntity(tTrItemReservation);

        return new ItemReservationDTO(
                createdTTrItemReservation.getItrsId(),
                ItemMapper.INSTANCE.entityToDTO(createdTTrItemReservation.getItem()),
                tTrItemReservation.getItrsStatus(),
                BigDecimal.valueOf(tTrItemReservation.getItrsQuantity())
        );
    }

    @Override
    public Boolean removeItemReservationByItemId(Long reservationId, Long itemId) {

        final TMsItem tMsItem = validateItemById(itemId);

        final TTrItemReservation tTrItemReservation = itemReservationRepository
                .findByReservation_ResvIdAndItrsStatusAndBranch_BrnhIdInAndItem_ItemId(reservationId,
                        STATUS_ACTIVE.getShortValue(), captureBranchIds(), itemId);

        tTrItemReservation.setItrsStatus(STATUS_INACTIVE.getShortValue());

        persistEntity(tTrItemReservation);

        return true;
    }

    @Override
    public List<ItemReservationDTO> getItemReservationsByReservation(Long reservationId) {

        final List<TTrItemReservation> tTrItemReservationList = itemReservationRepository
                .findAllByReservation_ResvIdAndItrsStatusAndBranch_BrnhIdIn(reservationId, STATUS_ACTIVE.getShortValue(), captureBranchIds());

        List<ItemReservationDTO> itemReservationDTOList = new ArrayList<>();

        tTrItemReservationList.forEach(tTrItemReservation -> {
            ItemReservationDTO itemReservationDTO = new ItemReservationDTO();

            itemReservationDTO.setItemReservationId(tTrItemReservation.getItrsId());
            itemReservationDTO.setItem(itemService.getItemById(tTrItemReservation.getItem().getItemId()));
            itemReservationDTO.setQuantity(BigDecimal.valueOf(tTrItemReservation.getItrsQuantity()));
            itemReservationDTO.setStatus(tTrItemReservation.getItrsStatus());

            itemReservationDTOList.add(itemReservationDTO);
        });

        return itemReservationDTOList;
    }

    @Override
    public Boolean cancelItemReservationsByReservation(Long reservationId) {

        final List<TTrItemReservation> tTrItemReservationList = itemReservationRepository
                .findAllByReservation_ResvIdAndItrsStatusAndBranch_BrnhIdIn(reservationId, STATUS_ACTIVE.getShortValue(), captureBranchIds());

        tTrItemReservationList.forEach(tTrItemReservation -> {

            tTrItemReservation.setItrsStatus(STATUS_INACTIVE.getShortValue());

            persistEntity(tTrItemReservation);
        });

        return true;
    }

    @Override
    public BigDecimal calculateItemReservationAmount(Long reservationId) {

        BigDecimal totalItemReservationAmount = BigDecimal.ZERO;

        final List<TTrItemReservation> tTrItemReservationList = itemReservationRepository
                .findAllByReservation_ResvIdAndItrsStatusAndBranch_BrnhIdIn(reservationId, STATUS_ACTIVE.getShortValue(), captureBranchIds());

        totalItemReservationAmount = tTrItemReservationList.stream()
                .map(tTrItemReservation -> tTrItemReservation.getItem().getItemPrice().multiply(BigDecimal.valueOf(tTrItemReservation.getItrsQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalItemReservationAmount;
    }

    private TMsItem validateItemById(Long itemId) {

        if(itemId == null)
            throw new NoRequiredInfoException("Item Id is required");

        final TMsItem tMsItem = itemRepository
                .getByItemIdAndBranch_BrnhIdInAndItemStatus(itemId, captureBranchIds(), STATUS_ACTIVE.getShortValue());

        if(tMsItem == null)
            throw new DataNotFoundException("Item not found for the Id " + itemId);

        return tMsItem;
    }

    private TTrItemReservation persistEntity(TTrItemReservation tTrItemReservation) {
        try {
            validateEntity(tTrItemReservation);
            return itemReservationRepository.save(tTrItemReservation);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TransactionConflictException("Transaction Updated by Another User.");
        } catch (Exception e) {
            log.error("Error while persisting : " + e.getMessage());
            throw new OperationException(e.getMessage());
        }
    }
}
