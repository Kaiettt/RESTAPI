package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @PostMapping("/user")
    public User createNewUser(@RequestBody User postManUser) {
        User user = this.userService.handleSaveUser(postManUser);
        return user;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        this.userService.handleDeleteUser(id);
        return "Delete";
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable("id") long id) {
        return this.userService.getUserById(id);
    }

    @GetMapping("/user")
    public List<User> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @PutMapping("/user")
    public User updateUser(@RequestBody User updatedUser) {
        return this.userService.handleUpdateUser(updatedUser);
    }
}
