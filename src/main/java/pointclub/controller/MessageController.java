package pointclub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pointclub.entity.Message;
import pointclub.repository.MessageRepository;

import java.util.List;

@RestController
@RequestMapping("message")
public class MessageController {
    @Autowired
    private MessageRepository messageRepository;

    @GetMapping
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    @PostMapping("add")
    public void addMessage(@RequestBody Message message) {
        messageRepository.save(message);
    }

    @PostMapping("remove")
    public void removeMessage(@RequestBody Message message) {
        if (message.getId() == 0) {
            throw new RuntimeException("Message id cannot be null on delete");
        }
        messageRepository.deleteById(message.getId());
    }
}
