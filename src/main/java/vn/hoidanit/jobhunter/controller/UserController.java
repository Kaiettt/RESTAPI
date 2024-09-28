package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.service.error.HandleNumber;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;

import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/")
    public String getHelloWorld() {
        return "asdsd";
    }

    @PostMapping("/users")
    public ResponseEntity<User> createNewUser(@RequestBody User postManUser) {
        User user = this.userService.handleSaveUser(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") String id) throws IdInvalidException {
        if (!HandleNumber.isNumberic(id)) {
            throw new IdInvalidException("Can pass vao mot con so");
        }

        Long real_id = Long.valueOf(id);
        if (real_id >= 1500) {
            throw new IdInvalidException("ID khong hop le");
        }

        this.userService.handleDeleteUser(real_id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete User Sucessfully");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String id) throws IdInvalidException {
        if (!HandleNumber.isNumberic(id)) {
            throw new IdInvalidException("Can pass vao mot con so");
        }
        Long real_id = Long.valueOf(id);
        if (real_id >= 1500) {
            throw new IdInvalidException("ID khong hop le");
        }
        User user = this.userService.getUserById(real_id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = this.userService.getAllUsers();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User updatedUser) {
        User user = this.userService.handleUpdateUser(updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
