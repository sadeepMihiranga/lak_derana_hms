package lk.lakderana.hms.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="T_MS_ITEM")
public class TMsItem extends AuditModel {

    @javax.persistence.Id
    @GeneratedValue(generator = "ItemSequence")
    @SequenceGenerator(name = "ItemSequence", schema = "LAKDERANA_BASE", sequenceName = "\"T_MS_ITEM_ITEM_ID_seq\"", allocationSize = 1)
    @Column(name = "ITEM_ID")
    private Long itemId;

    @Column(name = "ITEM_NAME")
    private String itemName;

    @Column(name = "ITEM_TYPE_CODE")
    private String itemTypeCode;

    @Column(name = "ITEM_PRICE")
    private BigDecimal itemPrice;

    @Column(name = "ITEM_UOM")
    private String itemUom;

    @Column(name = "ITEM_STATUS")
    private Short itemStatus;

    @JoinColumn(name = "ITEM_BRANCH_ID")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private TRfBranch branch;
}
