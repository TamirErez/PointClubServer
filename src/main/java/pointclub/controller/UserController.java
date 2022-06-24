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
    public void addUser(@RequestBody User newUser) {
        userRepository.save(newUser);
    }

    @PostMapping("remove")
    public void removeUser(@RequestBody User userToRemove) {
        if (userToRemove.getId() == 0) {
            throw new RuntimeException("User id cannot be null on delete");
        }
        userRepository.deleteById(userToRemove.getId());
    }
}
