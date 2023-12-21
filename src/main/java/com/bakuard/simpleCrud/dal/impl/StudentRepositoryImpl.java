package com.bakuard.simpleCrud.dal.impl;

import com.bakuard.simpleCrud.dal.Page;
import com.bakuard.simpleCrud.dal.StudentRepository;
import com.bakuard.simpleCrud.model.Student;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

public class StudentRepositoryImpl implements StudentRepository {

    private final JdbcClient jdbcClient;

    public StudentRepositoryImpl(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
    }

    @Override
    public Page<Student> getAll(int pageNumber, int pageSize) {
        return null;
    }

    @Override
    public Student add(Student newStudent) {
        return null;
    }

    @Override
    public void deleteById(long id) {

    }
}
