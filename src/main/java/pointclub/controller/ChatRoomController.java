package pointclub.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pointclub.entity.User;
import pointclub.entity.chat.ChatRoom;
import pointclub.entity.chat.ChatRoomWithUser;
import pointclub.entity.chat.Message;
import pointclub.repository.ChatRoomRepository;
import pointclub.service.ChatService;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("chatRoom")
public class ChatRoomController {
    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatService chatService;

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
        chatService.removeChatRoom(chatRoomToRemove);
    }

    @PostMapping("/addUser")
    public void addUserToChatRoom(@RequestBody ChatRoomWithUser chatRoomWithUser) {
        chatService.addUserToChatRoom(chatRoomWithUser);
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
