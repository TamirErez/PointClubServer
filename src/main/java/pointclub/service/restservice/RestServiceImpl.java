package pointclub.service.restservice;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pointclub.entity.User;
import pointclub.repository.RoomRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class RestServiceImpl implements RestService {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void postToRoom(String messageContent, int id, String sender) throws FirebaseMessagingException {

        Collection<String> userTokensOfRoom = getUserTokensOfRoom(id);
        if (userTokensOfRoom.size() == 0) {
            System.out.println("Error Posting Message to Room: No Tokens Were Found");
            return;
        }
        MulticastMessage message = MulticastMessage.builder()
                .putData("content", messageContent)
                .putData("sender", sender)
                .addAllTokens(userTokensOfRoom)
                .build();
        BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
        System.out.println(response.getSuccessCount() + " messages were sent successfully");
    }

    private Collection<String> getUserTokensOfRoom(int id) {
        return roomRepository.getById(id).getUsers().stream().map(User::getToken).collect(Collectors.toList());
    }
}
