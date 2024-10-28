package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.request.ReqloginDTO;
import vn.hoidanit.jobhunter.domain.res.RestLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.service.annotation.ApiMessage;
import vn.hoidanit.jobhunter.service.error.IdInvalidException;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    @ApiMessage("Login Successfully")
    public ResponseEntity<RestLoginDTO> login(@Valid @RequestBody ReqloginDTO loginDto) {
        // Nạp input gồm username/password vào Security
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword());

        // xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // create a token

        SecurityContextHolder.getContext().setAuthentication(authentication);

        RestLoginDTO res = new RestLoginDTO();
        User userDB = this.userService.getUserByUserName(loginDto.getUsername());
        if (userDB != null) {
            RestLoginDTO.UserLogin user = new RestLoginDTO.UserLogin();
            user.setEmail(userDB.getEmail());
            user.setId(userDB.getId());
            user.setName(userDB.getName());
            res.setUser(user);
        }
        String access_token = this.securityUtil.createAccessToken(authentication.getName(), res.getUser());
        res.setAccessToken(access_token);

        String refreshToken = this.securityUtil.createRefreshToken(loginDto.getUsername(), res);
        this.userService.handleUpdateRefreshToken(userDB, refreshToken);

        ResponseCookie resCookies = ResponseCookie.from("refresh-token", refreshToken).httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @GetMapping("/auth/account")
    @ApiMessage("Fetch Account")
    public ResponseEntity<RestLoginDTO.UserGetAccount> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        User user = this.userService.getUserByUserName(email);
        RestLoginDTO.UserLogin userLogin = new RestLoginDTO.UserLogin();
        RestLoginDTO.UserGetAccount userGetAccount = new RestLoginDTO.UserGetAccount();
        if (user != null) {
            userLogin.setEmail(user.getEmail());
            userLogin.setId(user.getId());
            userLogin.setName(user.getName());
            userGetAccount.setUser(userLogin);
        }
        return ResponseEntity.ok().body(userGetAccount);
    }

    @GetMapping("/auth/refresh")
    @ApiMessage("Fetch refresh token")
    public ResponseEntity<RestLoginDTO> getRefreshToken(
            @CookieValue(name = "refresh-token", defaultValue = "") String refresh_token)
            throws IdInvalidException {
        Jwt jwt = this.securityUtil.checkValidRefreshToken(refresh_token);
        String email = jwt.getSubject();
        User user = this.userService.FetchUserByEmailAndRefreshToken(email, refresh_token);
        if (user == null) {
            throw new IdInvalidException("Refresh token khong hop le");
        }
        RestLoginDTO res = new RestLoginDTO();
        RestLoginDTO.UserLogin userLogin = new RestLoginDTO.UserLogin();
        userLogin.setEmail(user.getEmail());
        userLogin.setId(user.getId());
        userLogin.setName(user.getName());
        res.setUser(userLogin);
        String access_token = this.securityUtil.createAccessToken(email, res.getUser());
        res.setAccessToken(access_token);

        String refreshToken = this.securityUtil.createRefreshToken(email, res);
        this.userService.handleUpdateRefreshToken(user, refreshToken);

        ResponseCookie resCookies = ResponseCookie.from("refresh-token", refreshToken).httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, resCookies.toString())
                .body(res);
    }

    @PostMapping("/auth/logout")
    @ApiMessage("Log out successfully")
    public ResponseEntity<Void> LogoutRequest() throws IdInvalidException {
        String email = SecurityUtil.getCurrentUserLogin().isPresent() ? SecurityUtil.getCurrentUserLogin().get() : "";
        if (email.equals("email")) {
            throw new IdInvalidException("Access token khong hop le");
        }
        User user = this.userService.getUserByUserName(email);
        if (user == null) {
            throw new IdInvalidException("Access token khong hop le");
        }
        this.userService.handleUpdateRefreshToken(user, null);
        ResponseCookie deleteCookies = ResponseCookie.from("refresh-token", null).httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookies.toString()).body(null);
    }
}
