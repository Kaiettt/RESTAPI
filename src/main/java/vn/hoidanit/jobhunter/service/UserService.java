package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.service.error.RemoteEntityNotFound;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.res.CompanyResponce;
import vn.hoidanit.jobhunter.domain.res.ResUpdateUserResponce;
import vn.hoidanit.jobhunter.domain.res.RestFetchUserResponce;
import vn.hoidanit.jobhunter.domain.res.RestNewUserResponce;
import vn.hoidanit.jobhunter.domain.res.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.service.error.EmailExistedException;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final CompanyService companyService;
    private final RoleService roleService;

    public UserService(RoleService roleService, UserRepository userRepository, PasswordEncoder passwordEncoder,
            CompanyService companyService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
        this.roleService = roleService;
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

    public RestNewUserResponce handleSaveUser(User user) throws EmailExistedException, RemoteEntityNotFound {
        if (this.getUserByUserName(user.getEmail()) != null) {
            throw new EmailExistedException("Email Already Existed");
        }
        Role role = this.roleService.getRoleById(user.getRole().getId());
        if (role == null) {
            throw new RemoteEntityNotFound("Role not found");
        }
        RestNewUserResponce userResponce = new RestNewUserResponce();
        CompanyResponce companyResponce = new CompanyResponce();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getCompany() != null) {
            Company company = this.companyService.getCompanyById(user.getCompany().getId());
            if (company != null) {
                user.setCompany(company);
                companyResponce.setId(company.getId());
                companyResponce.setName(company.getName());
            }
        }
        user.setRole(role);
        user = this.userRepository.save(user);

        userResponce.setAddress(user.getAddress());
        userResponce.setAge(user.getAge());
        userResponce.setCreatedAt(user.getCreatedAt());
        userResponce.setEmail(user.getEmail());
        userResponce.setGender(user.getGender());
        userResponce.setId(user.getId());
        userResponce.setName(user.getName());
        userResponce.setCompany(companyResponce);
        userResponce.setRole(user.getRole().getName());
        return userResponce;
    }

    public void handleDeleteUser(long id) throws RemoteEntityNotFound {
        if (this.userRepository.findById(id) == null) {
            throw new RemoteEntityNotFound("User not found");
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

    public RestFetchUserResponce FetchUserById(long id) throws RemoteEntityNotFound {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (!userOptional.isPresent()) {
            throw new RemoteEntityNotFound("User not existed");
        }
        User user = userOptional.get();
        RestFetchUserResponce userResponce = new RestFetchUserResponce();
        userResponce.setId(user.getId());
        userResponce.setName(user.getName());

        userResponce.setAge(user.getAge());
        userResponce.setGender(user.getGender());
        userResponce.setAddress(user.getAddress());
        userResponce.setRole(user.getRole().getName());
        return userResponce;
    }

    public ResultPaginationDTO getAllUsers(Specification spec, Pageable pageable) {
        Page<User> userPage = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
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
            userResponce.setRole(user.getRole().getName());
            RestFetchUsers.add(userResponce);
        }
        res.setResult(RestFetchUsers);
        return res;
    }

    public User updateUser(User user) {
        return this.userRepository.save(user);
    }

    public ResUpdateUserResponce handleUpdateUser(User updatedUser) throws RemoteEntityNotFound {
        User user = this.getUserById(updatedUser.getId());
        if (user == null) {
            throw new RemoteEntityNotFound("User not existed");
        }
        CompanyResponce companyResponce = new CompanyResponce();
        user.setName(updatedUser.getName());
        user.setGender(updatedUser.getGender());
        user.setAge(updatedUser.getAge());
        user.setAddress(updatedUser.getAddress());
        if (updatedUser.getCompany() != null) {
            Company company = this.companyService.getCompanyById(updatedUser.getCompany().getId());
            if (company != null) {
                user.setCompany(company);
                companyResponce.setId(company.getId());
                companyResponce.setName(company.getName());
            }
        }
        Role role = this.roleService.getRoleById(updatedUser.getRole().getId());
        if (role != null) {
            user.setRole(role);
        }
        user = this.userRepository.save(user);

        ResUpdateUserResponce userResponce = new ResUpdateUserResponce();
        userResponce.setId(user.getId());
        userResponce.setName(user.getName());
        userResponce.setAddress(user.getAddress());
        userResponce.setGender(user.getGender());
        userResponce.setAge(user.getAge());
        userResponce.setUpdatedAt(user.getUpdatedAt());
        userResponce.setRole(user.getRole().getName());
        return userResponce;
    }

    public User getUserByUserName(String username) {
        return this.userRepository.findByEmail(username);
    }
}
