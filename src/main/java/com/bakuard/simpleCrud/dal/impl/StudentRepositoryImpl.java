package com.bakuard.simpleCrud.dal.impl;

import com.bakuard.simpleCrud.dal.Page;
import com.bakuard.simpleCrud.dal.PageMeta;
import com.bakuard.simpleCrud.dal.StudentRepository;
import com.bakuard.simpleCrud.exception.DuplicateUserException;
import com.bakuard.simpleCrud.exception.UnknownGroupException;
import com.bakuard.simpleCrud.model.Group;
import com.bakuard.simpleCrud.model.Student;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.List;

public class StudentRepositoryImpl implements StudentRepository {

    private final JdbcClient jdbcClient;

    public StudentRepositoryImpl(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
    }

    @Override
    public Page<Student> getAll(int pageNumber, int pageSize) {
        int totalNumber = getTotalNumberOfStudents();
        PageMeta pageMeta = new PageMeta(pageSize, pageNumber, totalNumber);

        List<Student> students = jdbcClient.sql("""
                SELECT students.student_id,
                       students.first_name,
                       students.second_name,
                       students.middle_name,
                       students.birthday,
                       groups.group_id,
                       groups.name
                    FROM students
                    INNER JOIN groups ON students.group_id = groups.group_id
                    ORDER BY students.first_name, students.second_name
                    LIMIT :page_number
                    OFFSET :offset
                """)
                .param("page_number", pageMeta.pageNumber())
                .param("offset", pageMeta.offset())
                .query((ResultSet result, int row) ->
                        new Student()
                                .setId(result.getLong(1))
                                .setFirstName(result.getString(2))
                                .setSecondName(result.getString(3))
                                .setMiddleName(result.getString(4))
                                .setBirthday(result.getDate(5).toLocalDate())
                                .setGroup(
                                        new Group()
                                                .setId(result.getLong(6))
                                                .setName(result.getString(7))
                                )
                )
                .list();

        return new Page<>(pageMeta, students);
    }

    @Override
    public Student add(Student newStudent) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcClient.sql("""
                    INSERT INTO students(first_name,
                                         second_name,
                                         middle_name,
                                         birthday,
                                         group_id)
                        VALUES (:first_name, :second_name, :middle_name, :birthday, :group_id);
                    """)
                    .param("first_name", newStudent.getFirstName())
                    .param("second_name", newStudent.getSecondName())
                    .param("middle_name", newStudent.getMiddleName())
                    .param("birthday", newStudent.getBirthday())
                    .param("group_id", newStudent.getGroup().getId())
                    .update(keyHolder);
            newStudent.setId(keyHolder.getKeyAs(Long.class));
            return newStudent;
        } catch(DuplicateKeyException e) {
            throw new DuplicateUserException(
                    "User with such id, first, last or middle name already exists.", e);
        } catch(DataIntegrityViolationException e) {
            Group group = newStudent.getGroup();
            throw new UnknownGroupException(
                    "Unknown group with name=%s and id=%d"
                            .formatted(group.getName(), group.getId()), e);
        }
    }

    @Override
    public void deleteById(long id) {

    }


    private int getTotalNumberOfStudents() {
        return (int) jdbcClient.sql("SELECT Count(*) AS totalNumber FROM students;")
                .query()
                .singleValue();
    }
}
