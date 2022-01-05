package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.ItemDTO;
import lk.lakderana.hms.dto.ItemReservationDTO;

import java.math.BigDecimal;
import java.util.List;

public interface ItemReservationService {

    ItemReservationDTO reserveItem(Long reservationId, ItemDTO itemDTO);

    Boolean removeItemReservationByItemId(Long reservationId, Long itemId);

    List<ItemReservationDTO> getItemReservationsByReservation(Long reservationId);

    Boolean cancelItemReservationsByReservation(Long reservationId);

    BigDecimal calculateItemReservationAmount(Long reservationId);
}
