package com.bakuard.simpleCrud.dal;

import com.bakuard.simpleCrud.model.Student;

public interface StudentRepository {

    public Page<Student> getAll(int pageNumber, int pageSize);

    public Student add(Student newStudent);

    public void deleteById(long id);
}
