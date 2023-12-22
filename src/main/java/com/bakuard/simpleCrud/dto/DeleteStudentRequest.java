package com.bakuard.simpleCrud.dto;

public class DeleteStudentRequest {

    private long id;

    public DeleteStudentRequest() {}

    public long getId() {
        return id;
    }

    public DeleteStudentRequest setId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeleteStudentRequest that = (DeleteStudentRequest) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return "DeleteStudentRequest{" +
                "id=" + id +
                '}';
    }
}
