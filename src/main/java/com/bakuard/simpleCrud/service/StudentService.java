package com.bakuard.simpleCrud.service;

import com.bakuard.simpleCrud.dal.Page;
import com.bakuard.simpleCrud.dal.StudentRepository;
import com.bakuard.simpleCrud.model.Student;

public class StudentService {

    private final TransactionUtil transaction;
    private final ValidatorUtil validator;
    private final StudentRepository studentRepository;

    public StudentService(TransactionUtil transaction,
                          ValidatorUtil validator,
                          StudentRepository studentRepository) {
        this.transaction = transaction;
        this.validator = validator;
        this.studentRepository = studentRepository;
    }

    public Student add(Student newStudent) {
        return transaction.commit(() -> {
            validator.assertValid(newStudent);
            return studentRepository.add(newStudent);
        });
    }

    public Page<Student> getAll(int pageNumber, int pageSize) {
        return transaction.commit(() -> studentRepository.getAll(pageNumber, pageSize));
    }

    public Student tryGetById(long id) {
        return transaction.commit(() -> studentRepository.tryGetById(id));
    }

    public void tryDeleteById(long id) {
        transaction.commit(() -> studentRepository.tryDeleteById(id));
    }
}
