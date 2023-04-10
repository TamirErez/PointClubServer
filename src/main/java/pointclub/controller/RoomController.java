package pointclub.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;
import pointclub.entity.Message;
import pointclub.entity.Room;
import pointclub.entity.RoomWithUser;
import pointclub.entity.User;
import pointclub.repository.RoomRepository;
import pointclub.repository.UserRepository;
import pointclub.repository.UsersToRoomsRepository;
import pointclub.service.restservice.RestService;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("room")
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UsersToRoomsRepository usersToRoomsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestService restService;

    @GetMapping
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @PostMapping("add")
    public int addRoom(@RequestBody Room newRoom) {
        return roomRepository.save(newRoom).getServerId();
    }

    @PostMapping("remove")
    public void removeRoom(@RequestBody Room roomToRemove) {
        if (roomToRemove.getServerId() == 0) {
            throw new RuntimeException("Room id cannot be null on delete");
        }
        roomRepository.deleteById(roomToRemove.getServerId());
    }

    @PostMapping("/addUser")
    public void addUserToRoom(@RequestBody RoomWithUser roomWithUser) {
        addUserToRoomIfNotExist(roomWithUser);
        sendUserToRoom(roomWithUser);
    }

    private void sendUserToRoom(RoomWithUser roomWithUser) {
        try {
            restService.sendUserToRoom(
                    roomRepository.getById(roomWithUser.getRoom()),
                    userRepository.getById(roomWithUser.getUser())
            );
        } catch (FirebaseMessagingException e) {
            log.warn("Error sending user joined room", e);
        }
    }

    private void addUserToRoomIfNotExist(RoomWithUser roomWithUser) {
        if(!usersToRoomsRepository.exists(Example.of(roomWithUser))) {
            usersToRoomsRepository.save(roomWithUser);
        }
    }

    @PostMapping("/users")
    public Set<User> getRoomUsers(@RequestBody Room room) {
        return roomRepository.getById(room.getServerId()).getUsers();
    }

    @PostMapping("/messages")
    public Set<Message> getRoomMessages(@RequestBody Room room) {
        return roomRepository.getById(room.getServerId()).getMessages();
    }
}
