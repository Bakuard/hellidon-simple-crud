package com.bakuard.simpleCrud.dal.impl;

import com.bakuard.simpleCrud.conf.DIContainer;
import com.bakuard.simpleCrud.dal.GroupRepository;
import com.bakuard.simpleCrud.dal.Page;
import com.bakuard.simpleCrud.dal.PageMeta;
import com.bakuard.simpleCrud.dal.StudentRepository;
import com.bakuard.simpleCrud.exception.DuplicateStudentException;
import com.bakuard.simpleCrud.exception.UnknownStudentException;
import com.bakuard.simpleCrud.model.Group;
import com.bakuard.simpleCrud.model.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class StudentRepositoryImplTest {

    private DIContainer diContainer;
    private StudentRepository studentRepository;
    private GroupRepository groupRepository;
    private TransactionTemplate transactionTemplate;
    private List<Group> groups;

    private void commit(Executable executable) {
        transactionTemplate.execute(status -> {
            try {
                executable.execute();
                return null;
            } catch(Throwable e) {
                status.setRollbackOnly();
                if(e instanceof RuntimeException) throw (RuntimeException)e;
                else throw new RuntimeException(e);
            }
        });
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        diContainer = new DIContainer("test.properties");
        transactionTemplate = diContainer.transactionTemplate();
        studentRepository = diContainer.studentRepository();
        groupRepository = diContainer.groupRepository();
        groups = new ArrayList<>();
        commit(() -> {
            diContainer.flyway().migrate();
            Group group1 = groupRepository.add(new Group().setName("group1"));
            Group group2 = groupRepository.add(new Group().setName("group2"));
            groups.add(group1);
            groups.add(group2);
        });
    }

    @AfterEach
    public void afterEach() {
        diContainer.dataSource().close();
    }


    @DisplayName("""
            add(newStudent):
             there is not student with such combination of firstName, lastName and middleName
             => add student
            """)
    @Test
    void add1() {
        Student student = new Student()
                .setFirstName("first1")
                .setSecondName("second1")
                .setMiddleName("middle1")
                .setBirthday(LocalDate.of(2022, 12, 2))
                .setGroup(groups.getFirst());

        commit(() -> studentRepository.add(student));
        Student actual = studentRepository.tryGetById(student.getId());

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(student);
    }

    @DisplayName("""
            add(newStudent):
             there is student with such combination of firstName, lastName and middleName
             => exception
            """)
    @Test
    void add2() {
        commit(() -> studentRepository.add(
                new Student()
                        .setFirstName("first1")
                        .setSecondName("second1")
                        .setMiddleName("middle1")
                        .setBirthday(LocalDate.of(2022, 12, 2))
                        .setGroup(groups.getFirst())
        ));

        Student duplicate = new Student()
                .setFirstName("first1")
                .setSecondName("second1")
                .setMiddleName("middle1")
                .setBirthday(LocalDate.of(2000, 12, 2))
                .setGroup(groups.getLast());

        Assertions.assertThatThrownBy(() -> commit(() -> studentRepository.add(duplicate)))
                .isInstanceOf(DuplicateStudentException.class);
    }

    @DisplayName("""
            add(newStudent):
             student hasn't group
             => add student
            """)
    @Test
    void add3() {
        Student student = new Student()
                .setFirstName("first1")
                .setSecondName("second1")
                .setMiddleName("middle1")
                .setBirthday(LocalDate.of(2022, 12, 2))
                .setGroup(null);

        commit(() -> studentRepository.add(student));
        Student actual = studentRepository.tryGetById(student.getId());

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(student);
    }

    @DisplayName("""
            getAll(pageNumber, pageSize):
             get first page,
             pageSize > totalItems
            """)
    @Test
    void getAll1() {
        commit(() -> IntStream.rangeClosed(1, 5)
                .forEach(
                        i -> studentRepository.add(
                                new Student()
                                        .setFirstName("first" + i)
                                        .setSecondName("second" + i)
                                        .setMiddleName("middle" + i)
                                        .setBirthday(LocalDate.of(2000, 12, 2))
                                        .setGroup(groups.getFirst())
                        )
                )
        );

        Page<Student> actual = studentRepository.getAll(0, 10);

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new Page<>(
                        new PageMeta(10, 0, 5),
                        IntStream.rangeClosed(1, 5)
                                .mapToObj(i ->
                                        new Student()
                                                .setId(i)
                                                .setFirstName("first" + i)
                                                .setSecondName("second" + i)
                                                .setMiddleName("middle" + i)
                                                .setBirthday(LocalDate.of(2000, 12, 2))
                                                .setGroup(groups.getFirst())
                                )
                                .toList()
                ));
    }

    @DisplayName("""
            getAll(pageNumber, pageSize):
             get middle page,
             pageSize < totalItems
            """)
    @Test
    void getAll2() {
        commit(() -> IntStream.rangeClosed(1, 100)
                .forEach(
                        i -> studentRepository.add(
                                new Student()
                                        .setFirstName("first" + i)
                                        .setSecondName("second" + i)
                                        .setMiddleName("middle" + i)
                                        .setBirthday(LocalDate.of(2000, 12, 2))
                                        .setGroup(groups.getFirst())
                        )
                )
        );

        Page<Student> actual = studentRepository.getAll(3, 10);

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new Page<>(
                        new PageMeta(10, 3, 100),
                        IntStream.rangeClosed(31, 40)
                                .mapToObj(i ->
                                        new Student()
                                                .setId(i)
                                                .setFirstName("first" + i)
                                                .setSecondName("second" + i)
                                                .setMiddleName("middle" + i)
                                                .setBirthday(LocalDate.of(2000, 12, 2))
                                                .setGroup(groups.getFirst())
                                )
                                .toList()
                ));
    }

    @DisplayName("""
            getAll(pageNumber, pageSize):
             get last page,
             pageSize < totalItems
            """)
    @Test
    void getAll3() {
        commit(() -> IntStream.rangeClosed(1, 100)
                .forEach(
                        i -> studentRepository.add(
                                new Student()
                                        .setFirstName("first" + i)
                                        .setSecondName("second" + i)
                                        .setMiddleName("middle" + i)
                                        .setBirthday(LocalDate.of(2000, 12, 2))
                                        .setGroup(groups.getFirst())
                        )
                )
        );

        Page<Student> actual = studentRepository.getAll(9, 10);

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new Page<>(
                        new PageMeta(10, 9, 100),
                        IntStream.rangeClosed(91, 100)
                                .mapToObj(i ->
                                        new Student()
                                                .setId(i)
                                                .setFirstName("first" + i)
                                                .setSecondName("second" + i)
                                                .setMiddleName("middle" + i)
                                                .setBirthday(LocalDate.of(2000, 12, 2))
                                                .setGroup(groups.getFirst())
                                )
                                .toList()
                ));
    }

    @DisplayName("""
            getAll(pageNumber, pageSize):
             page number > max page number,
             pageSize < totalItems
             => return last page
            """)
    @Test
    void getAll4() {
        commit(() -> IntStream.rangeClosed(1, 100)
                .forEach(
                        i -> studentRepository.add(
                                new Student()
                                        .setFirstName("first" + i)
                                        .setSecondName("second" + i)
                                        .setMiddleName("middle" + i)
                                        .setBirthday(LocalDate.of(2000, 12, 2))
                                        .setGroup(groups.getFirst())
                        )
                )
        );

        Page<Student> actual = studentRepository.getAll(1000, 10);

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new Page<>(
                        new PageMeta(10, 9, 100),
                        IntStream.rangeClosed(91, 100)
                                .mapToObj(i ->
                                        new Student()
                                                .setId(i)
                                                .setFirstName("first" + i)
                                                .setSecondName("second" + i)
                                                .setMiddleName("middle" + i)
                                                .setBirthday(LocalDate.of(2000, 12, 2))
                                                .setGroup(groups.getFirst())
                                )
                                .toList()
                ));
    }

    @DisplayName("""
            getAll(pageNumber, pageSize):
             there are not students in DB
             => return empty page
            """)
    @Test
    void getAll5() {
        Page<Student> actual = studentRepository.getAll(0, 10);

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new Page<>(
                        new PageMeta(10, 0, 0),
                        List.of()
                ));
    }

    @DisplayName("""
            getAll(pageNumber, pageSize):
             there are students without groups
             => return empty page
            """)
    @Test
    void getAll6() {
        commit(() -> {
            IntStream.rangeClosed(1, 15)
                            .forEach(
                                    i -> studentRepository.add(
                                            new Student()
                                                    .setFirstName("first" + i)
                                                    .setSecondName("second" + i)
                                                    .setMiddleName("middle" + i)
                                                    .setBirthday(LocalDate.of(2000, 12, 2))
                                                    .setGroup(groups.getFirst())
                                    )
                            );
            IntStream.rangeClosed(16, 30)
                    .forEach(
                            i -> studentRepository.add(
                                    new Student()
                                            .setFirstName("first" + i)
                                            .setSecondName("second" + i)
                                            .setMiddleName("middle" + i)
                                            .setBirthday(LocalDate.of(1995, 6, 18))
                                            .setGroup(null)
                            )
                    );
        });

        Page<Student> actual = studentRepository.getAll(1, 10);

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new Page<>(
                        new PageMeta(10, 1, 30),
                        Stream.concat(
                                IntStream.rangeClosed(11, 15)
                                        .mapToObj(i ->
                                                new Student()
                                                        .setId(i)
                                                        .setFirstName("first" + i)
                                                        .setSecondName("second" + i)
                                                        .setMiddleName("middle" + i)
                                                        .setBirthday(LocalDate.of(2000, 12, 2))
                                                        .setGroup(groups.getFirst())
                                        ),
                                IntStream.rangeClosed(16, 20)
                                        .mapToObj(i ->
                                                new Student()
                                                        .setId(i)
                                                        .setFirstName("first" + i)
                                                        .setSecondName("second" + i)
                                                        .setMiddleName("middle" + i)
                                                        .setBirthday(LocalDate.of(1995, 6, 18))
                                                        .setGroup(null)
                                        )
                        ).toList()
                ));
    }

    @DisplayName("""
            tryGetById(id):
             there is not student with such id
             => exception
            """)
    @Test
    void tryGetById() {
        commit(() -> IntStream.rangeClosed(1, 100)
                .forEach(
                        i -> studentRepository.add(
                                new Student()
                                        .setFirstName("first" + i)
                                        .setSecondName("second" + i)
                                        .setMiddleName("middle" + i)
                                        .setBirthday(LocalDate.of(2000, 12, 2))
                                        .setGroup(groups.getFirst())
                        )
                )
        );

        Assertions.assertThatThrownBy(() -> studentRepository.tryGetById(1000))
                .isInstanceOf(UnknownStudentException.class);
    }

    @DisplayName("""
            deleteById(id):
             there is user with such id
             => delete this user
            """)
    @Test
    void deleteById1() {
        commit(() -> IntStream.rangeClosed(1, 100)
                .forEach(
                        i -> studentRepository.add(
                                new Student()
                                        .setFirstName("first" + i)
                                        .setSecondName("second" + i)
                                        .setMiddleName("middle" + i)
                                        .setBirthday(LocalDate.of(2000, 12, 2))
                                        .setGroup(groups.getFirst())
                        )
                )
        );

        commit(() -> studentRepository.tryDeleteById(55));

        Assertions.assertThatThrownBy(() -> studentRepository.tryGetById(55))
                .isInstanceOf(UnknownStudentException.class);
    }

    @DisplayName("""
            deleteById(id):
             there is not user with such id
             => exception
            """)
    @Test
    void deleteById2() {
        commit(() -> IntStream.rangeClosed(1, 100)
                .forEach(
                        i -> studentRepository.add(
                                new Student()
                                        .setFirstName("first" + i)
                                        .setSecondName("second" + i)
                                        .setMiddleName("middle" + i)
                                        .setBirthday(LocalDate.of(2000, 12, 2))
                                        .setGroup(groups.getFirst())
                        )
                )
        );

        Assertions.assertThatThrownBy(() -> commit(() -> studentRepository.tryDeleteById(1000)))
                .isInstanceOf(UnknownStudentException.class);
    }
}
