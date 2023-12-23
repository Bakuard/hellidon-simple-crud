package com.bakuard.simpleCrud.service;

import com.bakuard.simpleCrud.dal.GroupRepository;
import com.bakuard.simpleCrud.dal.Page;
import com.bakuard.simpleCrud.model.Group;

public class GroupService {

    private final TransactionUtil transaction;
    private final ValidatorUtil validator;
    private final GroupRepository groupRepository;

    public GroupService(TransactionUtil transaction,
                        ValidatorUtil validator,
                        GroupRepository groupRepository) {
        this.transaction = transaction;
        this.validator = validator;
        this.groupRepository = groupRepository;
    }

    public Group add(Group newGroup) {
        return transaction.commit(() -> {
            validator.assertValid(newGroup);
            return groupRepository.add(newGroup);
        });
    }

    public Page<Group> getAll(int pageNumber, int pageSize) {
        return transaction.commit(() -> groupRepository.getAll(pageNumber, pageSize));
    }

    public Group tryGetById(long id) {
        return transaction.commit(() -> groupRepository.tryGetById(id));
    }
}
