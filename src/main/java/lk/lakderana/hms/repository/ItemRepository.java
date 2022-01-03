package lk.lakderana.hms.repository;

import lk.lakderana.hms.entity.TMsItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<TMsItem, Long> {

    @Query("SELECT t FROM TMsItem t " +
            "WHERE UPPER(t.itemName) LIKE CONCAT('%', UPPER(:itemName), '%') " +
            "AND UPPER(t.itemTypeCode) LIKE CONCAT('%', UPPER(:itemTypeCode), '%') " +
            "AND (:status IS NULL OR (:status IS NOT NULL AND t.itemStatus = :status)) " +
            "AND t.branch.brnhId IN :branchIdList " +
            "ORDER BY t.lastModUserCode")
    Page<TMsItem> searchItems(@Param("itemName") String itemName,
                              @Param("itemTypeCode") String itemTypeCode,
                              @Param("branchIdList") List<Long> branchIdList,
                              @Param("status") Short status,
                              Pageable pageable);

    TMsItem getByItemIdAndBranch_BrnhIdInAndItemStatus(Long itemId, List<Long> brnhIdList, Short itemStatus);
}
