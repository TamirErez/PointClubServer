package pointclub.service.restservice;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pointclub.entity.Message;
import pointclub.entity.User;
import pointclub.repository.RoomRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class RestServiceImpl implements RestService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void postToRoom(Message message) throws FirebaseMessagingException {

        Collection<String> userTokensOfRoom = getUserTokensOfRoom(message.getRoom().getServerId(), message.getSender());
        if (userTokensOfRoom.size() == 0) {
            System.out.println("Error Posting Message to Room: No Tokens Were Found");
            return;
        }
        MulticastMessage multicastMessage = MulticastMessage.builder()
                .putData("content", message.getContent())
                .putData("sender", String.valueOf(message.getSender().getServerId()))
                .putData("id", String.valueOf(message.getServerId()))
                .putData("sendTime", String.valueOf(message.getSendTime().getTime()))
                .addAllTokens(userTokensOfRoom)
                .build();
        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(multicastMessage);
        System.out.println(response.getSuccessCount() + " messages were sent successfully");
    }

    private Collection<String> getUserTokensOfRoom(int id, User sender) {
        return roomRepository.getById(id)
                .getUsers().stream()
                .filter(user -> !user.equals(sender))
                .map(User::getToken)
                .collect(Collectors.toList());
    }
}
