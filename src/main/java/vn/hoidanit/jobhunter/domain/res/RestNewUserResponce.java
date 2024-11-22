package vn.hoidanit.jobhunter.domain.res;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonFormat;

import vn.hoidanit.jobhunter.util.constant.GenderEnum;

public class RestNewUserResponce {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private String role;
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant createdAt;
    private CompanyResponce company;

    public RestNewUserResponce() {

    }

    public RestNewUserResponce(long id, String name, String email, GenderEnum gender, String address, int age,
            Instant createdAt, CompanyResponce company,String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.address = address;
        this.age = age;
        this.createdAt = createdAt;
        this.company = company;
        this.role = role;
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

    public GenderEnum getGender() {
        return gender;
    }

    public void setGender(GenderEnum gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public CompanyResponce getCompany() {
        return company;
    }

    public void setCompany(CompanyResponce company) {
        this.company = company;
    }

}
