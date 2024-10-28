package vn.hoidanit.jobhunter.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@Entity
@Table(name = "companies")
public class Company {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotBlank(message = "name should not be empty")
  private String name;

  @Column(columnDefinition = "MEDIUMTEXT")
  private String description;

  private String address;
  private String logo;
  private Instant createdAt;
  private Instant updatedAt;
  private String createdBy;
  private String updatedBy;

  @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
  @JsonIgnore
  List<User> users;

  @OneToMany(mappedBy = "company", fetch = FetchType.LAZY)
  @JsonIgnore
  List<Job> jobs;

  public Company() {}

  public Company(
      long id,
      @NotBlank(message = "name should not be empty") String name,
      String description,
      String address,
      String logo,
      Instant createdAt,
      Instant updatedAt,
      String createdBy,
      String updatedBy,
      List<User> users,
      List<Job> jobs) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.address = address;
    this.logo = logo;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.createdBy = createdBy;
    this.updatedBy = updatedBy;
    this.users = users;
    this.jobs = jobs;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getLogo() {
    return logo;
  }

  public void setLogo(String logo) {
    this.logo = logo;
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

  public void setUpdatedBy(String updatedBys) {
    this.updatedBy = updatedBys;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }
}
