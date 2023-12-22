package com.bakuard.simpleCrud.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public class Student {

    private long id;
    //Имя
    @NotBlank(message = "Student first name must contains at least one non-whitespace character")
    private String firstName;
    //Фамилия
    @NotBlank(message = "Student second name must contains at least one non-whitespace character")
    private String secondName;
    //Отчество
    @NotBlank(message = "Student middle name must contains at least one non-whitespace character")
    private String middleName;
    @NotNull
    @PastOrPresent(message = "Student birthday must be past or present")
    private LocalDate birthday;
    private Group group;

    public Student() {}

    public long getId() {
        return id;
    }

    public Student setId(long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public Student setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getSecondName() {
        return secondName;
    }

    public Student setSecondName(String secondName) {
        this.secondName = secondName;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public Student setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public Student setBirthday(LocalDate birthday) {
        this.birthday = birthday;
        return this;
    }

    public Group getGroup() {
        return group;
    }

    public Student setGroup(Group group) {
        this.group = group;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthday=" + birthday +
                ", groupName='" + group + '\'' +
                '}';
    }
}
