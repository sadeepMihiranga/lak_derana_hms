package lk.lakderana.hms.service;

import lk.lakderana.hms.dto.ItemDTO;
import lk.lakderana.hms.dto.PaginatedEntity;

public interface ItemService {

    ItemDTO createItem(ItemDTO itemDTO);

    Boolean removeItem(Long itemId);

    ItemDTO getItemById(Long itemId);

    ItemDTO updateItem(Long itemId, ItemDTO itemDTO);

    PaginatedEntity itemPaginatedSearch(String name, String itemTypeCode, Short status, Integer page, Integer size);
}
