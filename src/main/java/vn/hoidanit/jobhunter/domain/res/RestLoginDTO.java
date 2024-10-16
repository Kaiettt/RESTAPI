package vn.hoidanit.jobhunter.domain.res;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestLoginDTO {
    @JsonProperty("access_token")
    private String accessToken;
    private UserLogin user;

    public RestLoginDTO() {
    }

    public static class UserGetAccount {
        private UserLogin user;

        public UserLogin getUser() {
            return user;
        }

        public void setUser(UserLogin user) {
            this.user = user;
        }

    }

    public static class UserLogin {
        private long id;
        private String name;
        private String email;

        public UserLogin() {

        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public UserLogin getUser() {
        return user;
    }

    public void setUser(UserLogin user) {
        this.user = user;
    }

}
