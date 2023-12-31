package com.bakuard.simpleCrud.dto;

import java.util.Objects;

public class StudentResponse {

    private long id;
    //Имя
    private String firstName;
    //Фамилия
    private String secondName;
    //Отчество
    private String middleName;
    private String birthday;
    private GroupResponse group;

    public StudentResponse() {}

    public long getId() {
        return id;
    }

    public StudentResponse setId(long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public StudentResponse setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getSecondName() {
        return secondName;
    }

    public StudentResponse setSecondName(String secondName) {
        this.secondName = secondName;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public StudentResponse setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public StudentResponse setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public GroupResponse getGroup() {
        return group;
    }

    public StudentResponse setGroup(GroupResponse group) {
        this.group = group;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentResponse that = (StudentResponse) o;
        return id == that.id &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(secondName, that.secondName) &&
                Objects.equals(middleName, that.middleName) &&
                Objects.equals(birthday, that.birthday) &&
                Objects.equals(group, that.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, secondName, middleName, birthday, group);
    }

    @Override
    public String toString() {
        return "StudentListItemResponse{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthday=" + birthday +
                ", group=" + group +
                '}';
    }
}
