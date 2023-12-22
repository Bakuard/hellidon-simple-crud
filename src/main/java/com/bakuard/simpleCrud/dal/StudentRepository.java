package com.bakuard.simpleCrud.dal;

import com.bakuard.simpleCrud.model.Student;

public interface StudentRepository {

    public Student add(Student newStudent);

    public Page<Student> getAll(int pageNumber, int pageSize);

    public Student tryGetById(long id);

    public void tryDeleteById(long id);
}
