package vn.hoidanit.jobhunter.domain;


import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.EnumType;
import lombok.experimental.SuperBuilder;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.constant.ResumeStateEnum;
@SuperBuilder
@Entity
@Table(name = "resumes")
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String email;
    private String url;
    @Enumerated(EnumType.STRING)
    private ResumeStateEnum status;
    private Instant createdAt;

    private Instant updatedAt;

    private String createdBy;
    private String updatedBy;


    
    public Resume() {
    }


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    @JsonIgnore
    private Job job;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    public Resume(Instant createdAt, String createdBy, String email, Long id, Job job, ResumeStateEnum status, Instant updatedAt, String updatedBy, String url, User user) {
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.email = email;
        this.id = id;
        this.job = job;
        this.status = status;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
        this.url = url;
        this.user = user;
    }

        @PrePersist
        public void handleBeforCreate() {
            this.createdBy =
                SecurityUtil.getCurrentUserLogin().isPresent() == true
                    ? SecurityUtil.getCurrentUserLogin().get()
                    : "";

            this.createdAt = Instant.now();
        }

        @PreUpdate
        public void handleBeforUpdate() {
            this.updatedBy =
                SecurityUtil.getCurrentUserLogin().isPresent() == true
                    ? SecurityUtil.getCurrentUserLogin().get()
                    : "";

        this.updatedAt = Instant.now();
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


    public Job getJob() {
        return job;
    }


    public void setJob(Job job) {
        this.job = job;
    }


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }


}

