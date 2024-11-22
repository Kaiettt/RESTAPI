package vn.hoidanit.jobhunter.domain.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.Instant;

import vn.hoidanit.jobhunter.util.constant.GenderEnum;

public class ResUpdateUserResponce {
    private long id;
    private String name;
    private GenderEnum gender;
    private String address;
    private int age;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss a", timezone = "GMT+7")
    private Instant updatedAt;
    private String role;
    public ResUpdateUserResponce() {

    }

    public ResUpdateUserResponce(String role,long id, String name, GenderEnum gender, String address, int age, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.age = age;
        this.updatedAt = updatedAt;
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

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
