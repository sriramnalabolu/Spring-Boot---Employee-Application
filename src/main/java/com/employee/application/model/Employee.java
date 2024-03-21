package com.employee.application.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee {

    private int id;
    private String name;
    private int age;
    private String gender;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate joiningDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate retiringDate;

    private String dept;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setJoiningDate(LocalDate joiningDate) {
        this.joiningDate = joiningDate;
    }

    public void setRetiringDate(LocalDate retiringDate) {
        this.retiringDate = retiringDate;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    // Getter methods for all properties
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getJoiningDate() {
        return joiningDate;
    }

    public LocalDate getRetiringDate() {
        return retiringDate;
    }

    public String getDept() {
        return dept;
    }
}
