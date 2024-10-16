package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.persistence.EntityNotFoundException;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.res.ResUpdateUserResponce;
import vn.hoidanit.jobhunter.domain.res.RestFetchUserResponce;
import vn.hoidanit.jobhunter.domain.res.RestNewUserResponce;
import vn.hoidanit.jobhunter.domain.res.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.service.annotation.ApiMessage;
import vn.hoidanit.jobhunter.service.error.EmailExistedException;
import vn.hoidanit.jobhunter.service.error.HandleNumber;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;
import vn.hoidanit.jobhunter.service.error.UserExistedException;

import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public String getMethodName(@RequestParam String param) {
        return new String();
    }

    @GetMapping("/")
    @CrossOrigin
    public String getHelloWorld() {
        return "hoi dan it";
    }

    @PostMapping("/users")
    @ApiMessage("Create new User")
    public ResponseEntity<RestNewUserResponce> createNewUser(@RequestBody User postManUser)
            throws EmailExistedException {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.handleSaveUser(postManUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("DeleteU User")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id)
            throws IdInvalidException, EntityNotFoundException {
        // Check if the ID is numeric
        if (!HandleNumber.isNumberic(id)) {
            throw new IdInvalidException("The provided ID must be a numeric value.");
        }
        Long realId = Long.valueOf(id);

        // Validate if the ID is within an acceptable range
        if (realId >= 1500) {
            throw new IdInvalidException("Invalid ID. The ID must be less than 1500.");
        }

        // Call the service to handle user deletion
        this.userService.handleDeleteUser(realId);

        return ResponseEntity.ok(null);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Fetching User")
    public ResponseEntity<RestFetchUserResponce> getUser(@PathVariable("id") String id)
            throws IdInvalidException, EntityNotFoundException {
        if (!HandleNumber.isNumberic(id)) {
            throw new IdInvalidException("Can pass vao mot con so");
        }
        Long real_id = Long.valueOf(id);
        if (real_id >= 1500) {
            throw new IdInvalidException("ID khong hop le");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.FetchUserById(real_id));
    }

    @GetMapping("/users")
    @ApiMessage("Fetch All Users")
    public ResponseEntity<ResultPaginationDTO> getAllUsers(@Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.getAllUsers(spec, pageable));
    }

    @PutMapping("/users")
    @ApiMessage("Update User")
    public ResponseEntity<ResUpdateUserResponce> updateUser(@RequestBody User updatedUser)
            throws EntityNotFoundException {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.handleUpdateUser(updatedUser));
    }
}
