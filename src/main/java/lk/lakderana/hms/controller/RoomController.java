package lk.lakderana.hms.controller;

import lk.lakderana.hms.dto.RoomDTO;
import lk.lakderana.hms.response.SuccessResponse;
import lk.lakderana.hms.response.SuccessResponseHandler;
import lk.lakderana.hms.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/search")
    public ResponseEntity<SuccessResponse> getPaginatedRooms(@RequestParam(name = "roomType", required = false) String roomType,
                                                             @RequestParam(name = "roomCategory", required = false) String roomCategory,
                                                             @RequestParam(name = "status", required = false) Short status,
                                                             @RequestParam(name = "page", required = true) Integer page,
                                                             @RequestParam(name = "size", required = true) Integer size) {
        return SuccessResponseHandler.generateResponse(roomService.roomPaginatedSearch(roomType, roomCategory, status, page, size));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> createRoom(@RequestBody RoomDTO roomDTO) {
        return SuccessResponseHandler.generateResponse(roomService.createRoom(roomDTO));
    }

    @PutMapping("/{roomId}")
    public ResponseEntity<SuccessResponse> updateRoom(@RequestBody RoomDTO roomDTO, @PathVariable("roomId") Long roomId) {
        return SuccessResponseHandler.generateResponse(roomService.updateRoom(roomId, roomDTO));
    }

    @DeleteMapping("/{roomId}")
    public ResponseEntity<SuccessResponse> inactiveRoom(@PathVariable("roomId") Long roomId) {
        return SuccessResponseHandler.generateResponse(roomService.inactiveRoom(roomId));
    }

    @DeleteMapping("/{roomId}/remove")
    public ResponseEntity<SuccessResponse> removeRoom(@PathVariable("roomId") Long roomId) {
        return SuccessResponseHandler.generateResponse(roomService.removeRoom(roomId));
    }
}
