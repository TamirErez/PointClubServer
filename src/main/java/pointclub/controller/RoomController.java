package pointclub.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.*;
import pointclub.entity.chat.Message;
import pointclub.entity.chat.ChatRoom;
import pointclub.entity.chat.ChatRoomWithUser;
import pointclub.entity.User;
import pointclub.repository.ChatRoomRepository;
import pointclub.repository.UserRepository;
import pointclub.repository.UsersToChatRoomsRepository;
import pointclub.service.restservice.RestService;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("room")
public class RoomController {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UsersToChatRoomsRepository usersToChatRoomsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestService restService;

    @GetMapping
    public List<ChatRoom> getAllChatRooms() {
        return chatRoomRepository.findAll();
    }

    @PostMapping("add")
    public int addChatRoom(@RequestBody ChatRoom newChatRoom) {
        return chatRoomRepository.save(newChatRoom).getServerId();
    }

    @PostMapping("remove")
    public void removeChatRoom(@RequestBody ChatRoom chatRoomToRemove) {
        if (chatRoomToRemove.getServerId() == 0) {
            throw new RuntimeException("Room id cannot be null on delete");
        }
        chatRoomRepository.deleteById(chatRoomToRemove.getServerId());
    }

    @PostMapping("/addUser")
    public void addUserToChatRoom(@RequestBody ChatRoomWithUser chatRoomWithUser) {
        addUserToRoomIfNotExist(chatRoomWithUser);
        sendUserToRoom(chatRoomWithUser);
    }

    private void sendUserToRoom(ChatRoomWithUser chatRoomWithUser) {
        try {
            restService.sendUserToRoom(
                    chatRoomRepository.getById(chatRoomWithUser.getRoom()),
                    userRepository.getById(chatRoomWithUser.getUser())
            );
        } catch (FirebaseMessagingException e) {
            log.warn("Error sending user joined room", e);
        }
    }

    private void addUserToRoomIfNotExist(ChatRoomWithUser chatRoomWithUser) {
        if(!usersToChatRoomsRepository.exists(Example.of(chatRoomWithUser))) {
            usersToChatRoomsRepository.save(chatRoomWithUser);
        }
    }

    @PostMapping("/users")
    public Set<User> getChatRoomUsers(@RequestBody ChatRoom chatRoom) {
        return chatRoomRepository.getById(chatRoom.getServerId()).getUsers();
    }

    @PostMapping("/messages")
    public Set<Message> getChatRoomMessages(@RequestBody ChatRoom chatRoom) {
        return chatRoomRepository.getById(chatRoom.getServerId()).getMessages();
    }
}
