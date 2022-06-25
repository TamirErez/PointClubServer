package pointclub.service.restservice;

import com.google.firebase.messaging.FirebaseMessagingException;

public interface RestService {
    void postToRoom(String data, int id, String sender) throws FirebaseMessagingException;
}
