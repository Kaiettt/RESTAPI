package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.DTO.Meta;
import vn.hoidanit.jobhunter.domain.DTO.ResUpdateUserResponce;
import vn.hoidanit.jobhunter.domain.DTO.RestFetchUserResponce;
import vn.hoidanit.jobhunter.domain.DTO.RestNewUserResponce;
import vn.hoidanit.jobhunter.domain.DTO.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.service.error.EmailExistedException;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;
import vn.hoidanit.jobhunter.service.error.UserExistedException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void handleUpdateRefreshToken(User user, String token) {
        if (user != null) {
            user.setRefreshToken(token);
            user = this.userRepository.save(user);
        }
    }

    public User FetchUserByEmailAndRefreshToken(String email, String refreshToken) {
        return this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }

    public RestNewUserResponce handleSaveUser(User user) throws EmailExistedException {
        if (this.getUserByUserName(user.getEmail()) != null) {
            throw new EmailExistedException("Email Already Existed");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = this.userRepository.save(user);
        RestNewUserResponce userResponce = new RestNewUserResponce();
        userResponce.setAddress(user.getAddress());
        userResponce.setAge(user.getAge());
        userResponce.setCreatedAt(user.getCreatedAt());
        userResponce.setEmail(user.getEmail());
        userResponce.setGender(user.getGender());
        userResponce.setId(user.getId());
        userResponce.setName(user.getName());
        return userResponce;
    }

    public void handleDeleteUser(long id) throws EntityNotFoundException {
        if (this.userRepository.findById(id) == null) {
            throw new EntityNotFoundException("User not found");
        }
        this.userRepository.deleteById(id);
    }

    public User getUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public RestFetchUserResponce FetchUserById(long id) throws EntityNotFoundException {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new EntityNotFoundException("User not existed");
        }
        User user = userOptional.get();
        RestFetchUserResponce userResponce = new RestFetchUserResponce();
        userResponce.setId(user.getId());
        userResponce.setName(user.getName());

        userResponce.setAge(user.getAge());
        userResponce.setGender(user.getGender());
        userResponce.setAddress(user.getAddress());

        return userResponce;
    }

    public ResultPaginationDTO getAllUsers(Specification spec, Pageable pageable) {
        Page<User> userPage = this.userRepository.findAll(spec, pageable);
        Meta meta = new Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(userPage.getTotalPages());
        meta.setTotal(userPage.getTotalElements());
        ResultPaginationDTO res = new ResultPaginationDTO();
        res.setMeta(meta);
        List<User> users = userPage.getContent();
        List<RestFetchUserResponce> RestFetchUsers = new ArrayList<>();
        for (User user : users) {
            RestFetchUserResponce userResponce = new RestFetchUserResponce();
            userResponce.setId(user.getId());
            userResponce.setName(user.getName());
            userResponce.setEmail(user.getEmail());
            userResponce.setAge(user.getAge());
            userResponce.setGender(user.getGender());
            userResponce.setAddress(user.getAddress());
            RestFetchUsers.add(userResponce);
        }
        res.setResult(RestFetchUsers);
        return res;
    }

    public User updateUser(User user) {
        return this.userRepository.save(user);
    }

    public ResUpdateUserResponce handleUpdateUser(User updatedUser) throws EntityNotFoundException {
        User user = this.getUserById(updatedUser.getId());
        if (user == null) {
            throw new EntityNotFoundException("User not existed");
        }
        user.setName(updatedUser.getName());
        user.setGender(updatedUser.getGender());
        user.setAge(updatedUser.getAge());
        user.setAddress(updatedUser.getAddress());
        user = this.userRepository.save(user);

        ResUpdateUserResponce userResponce = new ResUpdateUserResponce();
        userResponce.setId(user.getId());
        userResponce.setName(user.getName());
        userResponce.setAddress(user.getAddress());
        userResponce.setGender(user.getGender());
        userResponce.setAge(user.getAge());
        userResponce.setUpdatedAt(user.getUpdatedAt());
        return userResponce;
    }

    public User getUserByUserName(String username) {
        return this.userRepository.findByEmail(username);
    }
}
