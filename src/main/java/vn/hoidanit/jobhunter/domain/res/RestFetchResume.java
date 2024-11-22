package vn.hoidanit.jobhunter.domain.res;

import java.time.Instant;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import vn.hoidanit.jobhunter.util.constant.ResumeStateEnum;

public class RestFetchResume {
    private Long id;
    private String email;
    private String url;
    @Enumerated(EnumType.STRING)
    private ResumeStateEnum status;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private JobResume job;
    private UserResume user;
    private String companyName;
    public RestFetchResume(String companyName,Long id, String email, String url, ResumeStateEnum status, Instant createdAt,
            Instant updatedAt, String createdBy, String updatedBy, JobResume job, UserResume user) {
        this.id = id;
        this.companyName = companyName;
        this.email = email;
        this.url = url;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.job = job;
        this.user = user;
    }

   
    public static class JobResume {
        private long id;
        private String name;

        public JobResume() {
        }

        public JobResume(long id, String name) {
            this.id = id;
            this.name = name;
        }

        // Getters and Setters for JobResume fields
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
    }

    public static class UserResume {
        // Define fields and methods for UserResume as needed
        private long id;
        private String name;
        public UserResume() {
        }
        public UserResume(long id, String name) {
            this.id = id;
            this.name = name;
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
    }

    public RestFetchResume() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ResumeStateEnum getStatus() {
        return status;
    }

    public void setStatus(ResumeStateEnum status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public JobResume getJob() {
        return job;
    }

    public void setJob(JobResume job) {
        this.job = job;
    }

    public UserResume getUser() {
        return user;
    }

    public void setUser(UserResume user) {
        this.user = user;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


    // Getters and Setters for RestFetchResume fields (optional)
}
