package com.bakuard.simpleCrud.dal;

import com.bakuard.simpleCrud.model.Group;

public interface GroupRepository {

    public Group add(Group newGroup);

    public Page<Group> getAll(int pageNumber, int pageSize);

    public Group tryGetById(long id);
}
