package pointclub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pointclub.entity.User;
import pointclub.repository.UserRepository;

import java.util.List;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("add")
    public int addUser(@RequestBody User newUser) {
        return userRepository.save(newUser).getServerId();
    }

    @PutMapping("updateToken")
    public void updateToken(@RequestBody User user) {
        userRepository.save(user);
    }

    @PostMapping("remove")
    public void removeUser(@RequestBody User userToRemove) {
        if (userToRemove.getServerId() == 0) {
            throw new RuntimeException("User id cannot be null on delete");
        }
        userRepository.deleteById(userToRemove.getServerId());
    }
}
