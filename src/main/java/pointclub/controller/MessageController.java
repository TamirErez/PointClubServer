package pointclub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pointclub.service.restservice.RestService;
import pointclub.service.userservice.UserService;

@RestController()
@RequestMapping("message")
public class MessageController {
    @Autowired
    private RestService restService;
    @Autowired
    private UserService userService;


    @GetMapping("test")
    public void test() {
        // restService.postToRoom(message, userService.getRoomOfUser(requestingUserId).getId());
    }

    @PostMapping("/send")
    public void sendMessageToChat(@RequestParam String message, @RequestParam int requestingUserId) {
        restService.postToRoom(message, userService.getRoomOfUser(requestingUserId).getId());
    }
}
