package com.bakuard.simpleCrud.dal.impl;

import com.bakuard.simpleCrud.dal.GroupRepository;
import com.bakuard.simpleCrud.dal.Page;
import com.bakuard.simpleCrud.dal.PageMeta;
import com.bakuard.simpleCrud.exception.DuplicateGroupException;
import com.bakuard.simpleCrud.exception.UnknownGroupException;
import com.bakuard.simpleCrud.model.Group;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.ResultSet;
import java.util.List;

public class GroupRepositoryImpl implements GroupRepository {

    private final JdbcClient jdbcClient;

    public GroupRepositoryImpl(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
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
        long totalNumber = getTotalNumberOfGroups();
        PageMeta pageMeta = new PageMeta(pageSize, pageNumber, totalNumber);

        List<Group> groups = jdbcClient.sql("""
                SELECT groups.*
                    FROM groups
                    ORDER BY group_id
                    LIMIT :page_size
                    OFFSET :offset
                """)
                .param("page_size", pageMeta.pageSize())
                .param("offset", pageMeta.offset())
                .query((ResultSet result, int row) ->
                        new Group()
                                .setId(result.getLong("group_id"))
                                .setName(result.getString("name"))
                )
                .list();

        return new Page<>(pageMeta, groups);
    }

    @Override
    public Group tryGetById(long id) {
        return jdbcClient.sql("SELECT * FROM groups WHERE group_id = :id;")
                .param("id", id)
                .query((resultSet, rowNumber) ->
                        new Group()
                                .setId(resultSet.getLong("group_id"))
                                .setName(resultSet.getString("name"))
                )
                .optional()
                .orElseThrow(() -> new UnknownGroupException("Unknown group with id=" + id));
    }


    private long getTotalNumberOfGroups() {
        return (long) jdbcClient.sql("SELECT Count(*) AS totalNumber FROM groups;")
                .query()
                .singleValue();
    }
}
