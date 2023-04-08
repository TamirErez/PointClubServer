package pointclub.service.restservice;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pointclub.entity.Message;
import pointclub.entity.Room;
import pointclub.entity.User;
import pointclub.repository.RoomRepository;
import pointclub.repository.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class RestServiceImpl implements RestService {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void postToRoom(Message message) throws FirebaseMessagingException {

        Collection<String> userTokensOfRoom = getUserTokensOfRoom(message.getRoom().getServerId(), message.getSender());
        if (userTokensOfRoom.size() == 0) {
            System.out.println("Error Posting Message to Room: No Tokens Were Found");
            return;
        }
        MulticastMessage multicastMessage = MulticastMessage.builder()
                .putData("type", "message")
                .putData("content", message.getContent())
                .putData("sender", String.valueOf(message.getSender().getServerId()))
                .putData("id", String.valueOf(message.getServerId()))
                .putData("sendTime", String.valueOf(message.getSendTime().getTime()))
                .addAllTokens(userTokensOfRoom)
                .build();
        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(multicastMessage);
        System.out.println(response.getSuccessCount() + " messages were sent successfully");
    }

    @Override
    public void sendUserToRoom(Room room, User user) throws FirebaseMessagingException {
        Collection<String> userTokens = getUserTokensOfRoom(room.getServerId(), user);
        if (userTokens.size() == 0) {
            System.out.println("Error Sending User: No Tokens Were Found");
            return;
        }
        MulticastMessage multicastMessage = MulticastMessage.builder()
                .putData("type", "user")
                .putData("name", user.getName())
                .putData("serverId", String.valueOf(user.getServerId()))
                .putData("token", user.getToken())
                .addAllTokens(userTokens)
                .build();
        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(multicastMessage);
        System.out.println(response.getSuccessCount() + " messages were sent successfully");
    }

    private Collection<String> getUserTokens(User sender) {
        return getUserTokensWithoutSender(userRepository.findAll(), sender);
    }

    private Collection<String> getUserTokensOfRoom(int roomId, User sender) {
        return getUserTokensWithoutSender(roomRepository.getById(roomId).getUsers(), sender);

    }

    private Collection<String> getUserTokensWithoutSender(Collection<User> users, User sender) {
        return users.stream()
                .filter(user -> !user.equals(sender))
                .map(User::getToken)
                .collect(Collectors.toList());
    }
}
