package com.bakuard.simpleCrud.model;

import jakarta.validation.constraints.NotBlank;

public class Group {

    private long id;
    @NotBlank(message = "Group name must contains at least one non-whitespace character")
    private String name;

    public Group() {}

    public long getId() {
        return id;
    }

    public Group setId(long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Group setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
