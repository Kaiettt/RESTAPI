package vn.hoidanit.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import vn.hoidanit.jobhunter.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);

    User findById(User user);

    User findByEmail(String username);
}
