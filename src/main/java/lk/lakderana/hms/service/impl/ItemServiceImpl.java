package lk.lakderana.hms.service.impl;

import com.google.common.base.Strings;
import lk.lakderana.hms.config.EntityValidator;
import lk.lakderana.hms.dto.CommonReferenceDTO;
import lk.lakderana.hms.dto.ItemDTO;
import lk.lakderana.hms.dto.PaginatedEntity;
import lk.lakderana.hms.entity.TMsItem;
import lk.lakderana.hms.exception.DataNotFoundException;
import lk.lakderana.hms.exception.NoRequiredInfoException;
import lk.lakderana.hms.exception.OperationException;
import lk.lakderana.hms.exception.TransactionConflictException;
import lk.lakderana.hms.mapper.ItemMapper;
import lk.lakderana.hms.repository.ItemRepository;
import lk.lakderana.hms.service.CommonReferenceService;
import lk.lakderana.hms.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static lk.lakderana.hms.util.constant.CommonReferenceTypeCodes.ITEM_TYPES;
import static lk.lakderana.hms.util.constant.CommonReferenceTypeCodes.MEASUREMENT_TYPES;
import static lk.lakderana.hms.util.constant.Constants.STATUS_ACTIVE;
import static lk.lakderana.hms.util.constant.Constants.STATUS_INACTIVE;

@Slf4j
@Service
public class ItemServiceImpl extends EntityValidator implements ItemService {

    private final ItemRepository itemRepository;

    private final CommonReferenceService commonReferenceService;

    public ItemServiceImpl(ItemRepository itemRepository,
                           CommonReferenceService commonReferenceService) {
        this.itemRepository = itemRepository;
        this.commonReferenceService = commonReferenceService;
    }

    @Override
    public ItemDTO createItem(ItemDTO itemDTO) {

        validateEntity(itemDTO);
        validateReferenceData(itemDTO);

        itemDTO.setBranchId(captureBranchIds().get(0));
        TMsItem tMsItem = ItemMapper.INSTANCE.dtoToEntity(itemDTO);

        tMsItem.setItemStatus(STATUS_ACTIVE.getShortValue());

        return ItemMapper.INSTANCE.entityToDTO(persistEntity(tMsItem));
    }

    @Override
    public Boolean removeItem(Long itemId) {

        TMsItem tMsItem = validateItemId(itemId);

        tMsItem.setItemStatus(STATUS_INACTIVE.getShortValue());
        persistEntity(tMsItem);

        return true;
    }

    @Override
    public ItemDTO getItemById(Long itemId) {

        TMsItem tMsItem = validateItemId(itemId);

        return ItemMapper.INSTANCE.entityToDTO(tMsItem);
    }

    @Override
    public ItemDTO updateItem(Long itemId, ItemDTO itemDTO) {

        TMsItem tMsItem = validateItemId(itemId);
        validateReferenceData(itemDTO);

        tMsItem.setItemName(itemDTO.getName());
        tMsItem.setItemPrice(itemDTO.getPrice());
        tMsItem.setItemUom(itemDTO.getUom());
        tMsItem.setItemTypeCode(itemDTO.getItemTypeCode());

        return ItemMapper.INSTANCE.entityToDTO(persistEntity(tMsItem));
    }

    @Override
    public PaginatedEntity itemPaginatedSearch(String name, String itemTypeCode, Integer page, Integer size) {

        PaginatedEntity paginatedItemList = null;
        List<ItemDTO> itemList = null;

        validatePaginateIndexes(page, size);

        final Page<TMsItem> tMsItemPage = itemRepository
                .searchItems(name, itemTypeCode, captureBranchIds(), STATUS_ACTIVE.getShortValue(), PageRequest.of(page - 1, size));

        if (tMsItemPage.getSize() == 0)
            return null;

        paginatedItemList = new PaginatedEntity();
        itemList = new ArrayList<>();

        for (TMsItem tMsItem : tMsItemPage) {

            ItemDTO itemDTO = ItemMapper.INSTANCE.entityToDTO(tMsItem);
            setReferenceData(tMsItem, itemDTO);

            itemList.add(itemDTO);
        }

        paginatedItemList.setTotalNoOfPages(tMsItemPage.getTotalPages());
        paginatedItemList.setTotalNoOfRecords(tMsItemPage.getTotalElements());
        paginatedItemList.setEntities(itemList);

        return paginatedItemList;
    }

    private TMsItem validateItemId(Long itemId) {

        if(itemId == null)
            throw new NoRequiredInfoException("Item Id is required");

        final TMsItem tMsItem = itemRepository
                .getByItemIdAndBranch_BrnhIdInAndItemStatus(itemId, captureBranchIds(), STATUS_ACTIVE.getShortValue());

        if(tMsItem == null)
            throw new DataNotFoundException("Item not found for Id " + itemId);

        return tMsItem;
    }

    private void validateReferenceData(ItemDTO itemDTO) {

        commonReferenceService
                .getByCmrfCodeAndCmrtCode(ITEM_TYPES.getValue(), itemDTO.getItemTypeCode());
    }

    private void setReferenceData(TMsItem tMsItem, ItemDTO itemDTO) {

        if(tMsItem.getBranch() != null)
            itemDTO.setBranchName(tMsItem.getBranch().getBrnhName());

        if(!Strings.isNullOrEmpty(itemDTO.getItemTypeCode())) {
            final CommonReferenceDTO commonReferenceDTO = commonReferenceService
                    .getByCmrfCodeAndCmrtCode(ITEM_TYPES.getValue(), itemDTO.getItemTypeCode());

            itemDTO.setItemTypeName(commonReferenceDTO.getDescription());
        }

        final CommonReferenceDTO uomCommonReference = commonReferenceService
                .getByCmrfCodeAndCmrtCode(MEASUREMENT_TYPES.getValue(), itemDTO.getUom());

        itemDTO.setUomName(uomCommonReference.getDescription());
    }

    private TMsItem persistEntity(TMsItem tMsItem) {
        try {
            validateEntity(tMsItem);
            return itemRepository.save(tMsItem);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new TransactionConflictException("Transaction Updated by Another User.");
        } catch (Exception e) {
            log.error("Error while persisting : " + e.getMessage());
            throw new OperationException(e.getMessage());
        }
    }
}
