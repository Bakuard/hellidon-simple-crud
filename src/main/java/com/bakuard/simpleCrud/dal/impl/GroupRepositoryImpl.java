package com.bakuard.simpleCrud.dal.impl;

import com.bakuard.simpleCrud.dal.GroupRepository;
import com.bakuard.simpleCrud.dal.Page;
import com.bakuard.simpleCrud.dal.PageMeta;
import com.bakuard.simpleCrud.exception.DuplicateGroupException;
import com.bakuard.simpleCrud.model.Group;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.List;

public class GroupRepositoryImpl implements GroupRepository {

    private final JdbcClient jdbcClient;

    public GroupRepositoryImpl(DataSource dataSource) {
        this.jdbcClient = JdbcClient.create(dataSource);
    }

    @Override
    public Group add(Group newGroup) {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcClient.sql("""
                    INSERT INTO groups(name)
                        VALUES (:name);
                    """)
                    .param("name", newGroup.getName())
                    .update(keyHolder);
            newGroup.setId(keyHolder.getKeyAs(Long.class));
            return newGroup;
        } catch(DuplicateKeyException e) {
            throw new DuplicateGroupException("Group with such name already exists", e);
        }
    }

    @Override
    public Page<Group> getAll(int pageNumber, int pageSize) {
        int totalNumber = getTotalNumberOfGroups();
        PageMeta pageMeta = new PageMeta(pageSize, pageNumber, totalNumber);

        List<Group> groups = jdbcClient.sql("""
                SELECT groups.*
                    FROM groups
                    ORDER BY groups.name
                    LIMIT :page_number
                    OFFSET :offset
                """)
                .param("page_number", pageMeta.pageNumber())
                .param("offset", pageMeta.offset())
                .query((ResultSet result, int row) ->
                        new Group()
                                .setId(result.getLong("group_id"))
                                .setName(result.getString("name"))
                )
                .list();

        return new Page<>(pageMeta, groups);
    }


    private int getTotalNumberOfGroups() {
        return (int) jdbcClient.sql("SELECT Count(*) AS totalNumber FROM groups;")
                .query()
                .singleValue();
    }
}
