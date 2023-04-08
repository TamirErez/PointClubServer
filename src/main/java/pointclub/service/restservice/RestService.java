package pointclub.service.restservice;

import com.google.firebase.messaging.FirebaseMessagingException;
import pointclub.entity.Message;
import pointclub.entity.Room;
import pointclub.entity.User;

public interface RestService {
    void postToRoom(Message message) throws FirebaseMessagingException;

    void sendUserToRoom(Room room, User user) throws FirebaseMessagingException;
}
