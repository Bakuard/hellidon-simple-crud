package com.bakuard.simpleCrud.dto;

import java.util.Objects;

public class GroupResponse {

    private long id;
    private String name;

    public GroupResponse() {}

    public long getId() {
        return id;
    }

    public GroupResponse setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GroupResponse setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupResponse that = (GroupResponse) o;
        return id == that.id && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "GroupResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
