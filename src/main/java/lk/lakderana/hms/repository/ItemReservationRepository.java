package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TTrItemReservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemReservationRepository extends JpaRepository<TTrItemReservation, Long> {

    List<TTrItemReservation> findAllByReservation_ResvIdAndItrsStatusAndBranch_BrnhIdIn(Long resvId, Short itrsStatus,
                                                                                        List<Long> brnhIdList);

    TTrItemReservation findByReservation_ResvIdAndItrsStatusAndBranch_BrnhIdInAndItem_ItemId(Long resvId, Short itrsStatus,
                                                                                             List<Long> brnhIdList, Long itemId);
}
