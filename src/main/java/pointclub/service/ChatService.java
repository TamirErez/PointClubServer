package pointclub.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import pointclub.entity.chat.ChatRoom;
import pointclub.entity.chat.ChatRoomWithUser;
import pointclub.repository.ChatRoomRepository;
import pointclub.repository.UserRepository;
import pointclub.repository.UsersToChatRoomsRepository;
import pointclub.service.restservice.RestService;

@Slf4j
@Service
public class ChatService {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private UsersToChatRoomsRepository usersToChatRoomsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestService restService;

    public void removeChatRoom(@RequestBody ChatRoom chatRoomToRemove) {
        if (chatRoomToRemove.getServerId() == 0) {
            throw new RuntimeException("Room id cannot be null on delete");
        }
        chatRoomRepository.deleteById(chatRoomToRemove.getServerId());
    }

    public void addUserToChatRoom(@RequestBody ChatRoomWithUser chatRoomWithUser) {
        addUserToRoomIfNotExist(chatRoomWithUser);
        sendUserToRoom(chatRoomWithUser);
    }

    private void addUserToRoomIfNotExist(ChatRoomWithUser chatRoomWithUser) {
        if(!usersToChatRoomsRepository.exists(Example.of(chatRoomWithUser))) {
            usersToChatRoomsRepository.save(chatRoomWithUser);
        }
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
}
