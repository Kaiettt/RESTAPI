package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User handleSaveUser(User user) {
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        this.userRepository.deleteById(id);
    }

    public User getUserById(long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public User updateUser(User user) {
        return this.userRepository.save(user);
    }

    public User handleUpdateUser(User updatedUser) {
        User user = this.getUserById(updatedUser.getId());
        if (user != null) {
            user.setEmail(updatedUser.getEmail());
            user.setName(updatedUser.getName());
            user.setPassword(updatedUser.getPassword());
            this.userRepository.save(user);
        }
        return user;
    }
}
