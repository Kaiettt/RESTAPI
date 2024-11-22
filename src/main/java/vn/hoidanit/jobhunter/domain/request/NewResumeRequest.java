package vn.hoidanit.jobhunter.domain.request;

import jakarta.validation.constraints.NotBlank;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.util.constant.ResumeStateEnum;
public class NewResumeRequest {
    @NotBlank(message = "email should not be empty")
    private String email;
    @NotBlank(message = "url should not be empty")
    private String url;
    private ResumeStateEnum status;
    private User user;
    private Job job;
    public NewResumeRequest() {
    }
    public NewResumeRequest(String email, String url, ResumeStateEnum status, User user, Job job) {
        this.email = email;
        this.url = url;
        this.status = status;
        this.user = user;
        this.job = job;
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
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public Job getJob() {
        return job;
    }
    public void setJob(Job job) {
        this.job = job;
    }


}
