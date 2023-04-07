package pointclub.service.restservice;

import com.google.firebase.messaging.FirebaseMessagingException;
import pointclub.entity.Message;

public interface RestService {
    void postToRoom(Message message) throws FirebaseMessagingException;
}
