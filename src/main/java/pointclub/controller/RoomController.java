package pointclub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pointclub.entity.Room;
import pointclub.repository.RoomRepository;

import java.util.List;

@RestController
@RequestMapping("room")
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;

    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @PostMapping("/add")
    public void addRoom(@RequestBody Room newRoom) {
        roomRepository.save(newRoom);
    }

    @PostMapping("/remove")
    public void removeRoom(@RequestBody Room roomToRemove) {
        if (roomToRemove.getId() == 0) {
            throw new RuntimeException("Room id cannot be null on delete");
        }
        roomRepository.deleteById(roomToRemove.getId());
    }
}
