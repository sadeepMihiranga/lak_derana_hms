package lk.lakderana.hms.controller;

import lk.lakderana.hms.dto.ItemDTO;
import lk.lakderana.hms.response.SuccessResponse;
import lk.lakderana.hms.response.SuccessResponseHandler;
import lk.lakderana.hms.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse> getPaginatedItems(@RequestParam(name = "itemName", required = false) String itemName,
                                                             @RequestParam(name = "itemTypeCode", required = false) String itemTypeCode,
                                                             @RequestParam(name = "status", required = false) Short status,
                                                             @RequestParam(name = "page", required = true) Integer page,
                                                             @RequestParam(name = "size", required = true) Integer size) {
        return SuccessResponseHandler.generateResponse(itemService.itemPaginatedSearch(itemName, itemTypeCode, status, page, size));
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<SuccessResponse> getItem(@PathVariable("itemId") Long itemId) {
        return SuccessResponseHandler.generateResponse(itemService.getItemById(itemId));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> createItem(@RequestBody ItemDTO itemDTO) {
        return SuccessResponseHandler.generateResponse(itemService.createItem(itemDTO));
    }

    @PutMapping("/{itemId}")
    public ResponseEntity<SuccessResponse> updateItem(@RequestBody ItemDTO itemDTO, @PathVariable("itemId") Long itemId) {
        return SuccessResponseHandler.generateResponse(itemService.updateItem(itemId, itemDTO));
    }

    @DeleteMapping("/{itemId}/remove")
    public ResponseEntity<SuccessResponse> removeItem(@PathVariable("itemId") Long itemId) {
        return SuccessResponseHandler.generateResponse(itemService.removeItem(itemId));
    }
}
