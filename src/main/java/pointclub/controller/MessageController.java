package pointclub.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pointclub.entity.Message;
import pointclub.repository.MessageRepository;
import pointclub.service.restservice.RestService;

import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private RestService restService;

    @GetMapping
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @PostMapping("add")
    public void addMessage(@RequestBody Message message) {
        messageRepository.save(message);
        try {
            restService.postToRoom(message.getContent(), message.getRoom().getId(), message.getSender().getName());
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("remove")
    public void removeMessage(@RequestBody Message message) {
        if (message.getId() == 0) {
            throw new RuntimeException("Message id cannot be null on delete");
        }
        messageRepository.deleteById(message.getId());
    }
}
