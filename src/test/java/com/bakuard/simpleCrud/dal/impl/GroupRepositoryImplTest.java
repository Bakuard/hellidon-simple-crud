package com.bakuard.simpleCrud.dal.impl;

import com.bakuard.simpleCrud.conf.DIContainer;
import com.bakuard.simpleCrud.dal.GroupRepository;
import com.bakuard.simpleCrud.dal.Page;
import com.bakuard.simpleCrud.dal.PageMeta;
import com.bakuard.simpleCrud.exception.DuplicateGroupException;
import com.bakuard.simpleCrud.model.Group;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.transaction.support.TransactionTemplate;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

class GroupRepositoryImplTest {

    private DIContainer diContainer;
    private GroupRepository groupRepository;
    private TransactionTemplate transactionTemplate;

    private void commit(Executable executable) {
        transactionTemplate.execute(status -> {
            try {
                executable.execute();
                return null;
            } catch(Throwable e) {
                status.setRollbackOnly();
                throw new RuntimeException(e);
            }
        });
    }

    @BeforeEach
    public void beforeEach() throws IOException {
        diContainer = new DIContainer("test.properties");
        transactionTemplate = diContainer.transactionTemplate();
        groupRepository = diContainer.groupRepository();
        commit(() -> diContainer.flyway().migrate());
    }

    @AfterEach
    public void afterEach() {
        diContainer.dataSource().close();
    }


    @DisplayName("add(newGroup): there is not group with such name => add this group")
    @Test
    void add1() {
        Group group = new Group().setName("new Group");
        commit(() -> groupRepository.add(group));

        Group actual = groupRepository.getById(group.getId());

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(group);
    }

    @DisplayName("add(newGroup): there is group with such name => exception")
    @Test
    void add2() {
        commit(() -> groupRepository.add(new Group().setName("new Group")));

        Group duplicate = new Group().setName("new Group");

        Assertions.assertThatThrownBy(() -> groupRepository.add(duplicate))
                .isInstanceOf(DuplicateGroupException.class);
    }

    @DisplayName("""
            getAll(pageNumber, pageSize):
             get first page,
             pageSize > totalItems
            """)
    @Test
    void getAll1() {
        commit(() -> IntStream.range(1, 6)
                .forEach(
                        i -> groupRepository.add(new Group().setName("group" + i))
                )
        );

        Page<Group> actual = groupRepository.getAll(0, 10);

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new Page<>(
                        new PageMeta(10, 0, 5),
                        IntStream.range(1, 6)
                                .mapToObj(i -> new Group().setId(i).setName("group" + i))
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
                        i -> groupRepository.add(new Group().setName("group" + i))
                )
        );

        Page<Group> actual = groupRepository.getAll(3, 10);

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new Page<>(
                        new PageMeta(10, 3, 100),
                        IntStream.rangeClosed(31, 40)
                                .mapToObj(i -> new Group().setId(i).setName("group" + i))
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
                        i -> groupRepository.add(new Group().setName("group" + i))
                )
        );

        Page<Group> actual = groupRepository.getAll(9, 10);

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new Page<>(
                        new PageMeta(10, 9, 100),
                        IntStream.rangeClosed(91, 100)
                                .mapToObj(i -> new Group().setId(i).setName("group" + i))
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
                        i -> groupRepository.add(new Group().setName("group" + i))
                )
        );

        Page<Group> actual = groupRepository.getAll(1000, 10);

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new Page<>(
                        new PageMeta(10, 9, 100),
                        IntStream.rangeClosed(91, 100)
                                .mapToObj(i -> new Group().setId(i).setName("group" + i))
                                .toList()
                ));
    }

    @DisplayName("""
            getAll(pageNumber, pageSize):
             there are not groups in DB
             => return empty page
            """)
    @Test
    void getAll5() {
        Page<Group> actual = groupRepository.getAll(0, 10);

        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(new Page<>(
                        new PageMeta(10, 0, 0),
                        List.of()
                ));
    }
}