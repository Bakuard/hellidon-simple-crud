package com.bakuard.simpleCrud.dto;

import java.util.Objects;

public class NewGroupRequest {

    private String name;

    public NewGroupRequest() {}

    public String getName() {
        return name;
    }

    public NewGroupRequest setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewGroupRequest that = (NewGroupRequest) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "NewGroupRequest{" +
                "name='" + name + '\'' +
                '}';
    }
}
