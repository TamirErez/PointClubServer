package pointclub.service.restservice;

import com.google.firebase.messaging.FirebaseMessagingException;
import pointclub.entity.chat.Message;
import pointclub.entity.chat.ChatRoom;
import pointclub.entity.User;

public interface RestService {
    void postToRoom(Message message) throws FirebaseMessagingException;

    void sendUserToRoom(ChatRoom chatRoom, User user) throws FirebaseMessagingException;
}
