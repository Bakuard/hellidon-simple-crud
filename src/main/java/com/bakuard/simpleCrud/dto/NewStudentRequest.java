package com.bakuard.simpleCrud.dto;

import java.util.Objects;

public class NewStudentRequest {

    private String firstName;
    //Фамилия
    private String secondName;
    //Отчество
    private String middleName;
    private String birthday;
    private Long groupId;

    public NewStudentRequest() {

    }

    public String getFirstName() {
        return firstName;
    }

    public NewStudentRequest setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getSecondName() {
        return secondName;
    }

    public NewStudentRequest setSecondName(String secondName) {
        this.secondName = secondName;
        return this;
    }

    public String getMiddleName() {
        return middleName;
    }

    public NewStudentRequest setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public NewStudentRequest setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public Long getGroupId() {
        return groupId;
    }

    public NewStudentRequest setGroupId(Long groupId) {
        this.groupId = groupId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewStudentRequest that = (NewStudentRequest) o;
        return Objects.equals(firstName, that.firstName) &&
                Objects.equals(secondName, that.secondName) &&
                Objects.equals(middleName, that.middleName) &&
                Objects.equals(birthday, that.birthday) &&
                Objects.equals(groupId, that.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, secondName, middleName, birthday, groupId);
    }

    @Override
    public String toString() {
        return "NewStudentRequest{" +
                "firstName='" + firstName + '\'' +
                ", secondName='" + secondName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", birthday=" + birthday +
                ", groupId=" + groupId +
                '}';
    }
}
