package vn.hoidanit.jobhunter.domain;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@Entity
@Table(name = "permissions")
public class Permission {
        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "name should not be empty")
    private String name;
    private String apiPath;
    private String method;
    private String module;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;


    @ManyToMany(mappedBy = "permissions")
    @JsonIgnore
    List<Role> roles;
    // Constructors
    public Permission() {}

    
    public Permission(Long id, String name, String apiPath, String method, String module, Instant createdAt, Instant updatedAt, String createdBy, String updatedBy,List<Role> roles) {
        this.id = id;
        this.name = name;
        this.apiPath = apiPath;
        this.method = method;
        this.module = module;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.roles = roles;
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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
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

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", apiPath='" + apiPath + '\'' +
                ", method='" + method + '\'' +
                ", module='" + module + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", createdBy='" + createdBy + '\'' +
                ", updatedBy='" + updatedBy + '\'' +
                '}';
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
