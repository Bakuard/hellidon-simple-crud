package com.bakuard.simpleCrud.controller;

import com.bakuard.simpleCrud.dal.Page;
import com.bakuard.simpleCrud.dto.GroupResponse;
import com.bakuard.simpleCrud.dto.NewGroupRequest;
import com.bakuard.simpleCrud.dto.ResultMapper;
import com.bakuard.simpleCrud.model.Group;
import com.bakuard.simpleCrud.service.GroupService;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

public class GroupController implements HttpService {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public void routing(HttpRules httpRules) {
        httpRules.get("/{id}", this::getById)
                .get("/", this::getAll)
                .post("/new", this::add);
    }

    private void add(ServerRequest request, ServerResponse response) {
        NewGroupRequest newGroupRequest = request.content().as(NewGroupRequest.class);

        Group newGroup = ResultMapper.INSTANCE.toGroup(newGroupRequest);
        Group savedGroup = groupService.add(newGroup);
        GroupResponse groupResponse = ResultMapper.INSTANCE.toGroupResponse(savedGroup);

        response.status(200).send(groupResponse);
    }

    private void getById(ServerRequest request, ServerResponse response) {
        long groupId = Long.parseLong(
                request.path().pathParameters().get("id")
        );

        Group group = groupService.tryGetById(groupId);
        GroupResponse groupResponse = ResultMapper.INSTANCE.toGroupResponse(group);

        response.status(200).send(groupResponse);
    }

    private void getAll(ServerRequest request, ServerResponse response) {
        int pageNumber = Integer.parseInt(request.query().get("pageNumber"));
        int pageSize = Integer.parseInt(request.query().get("pageSize"));

        Page<Group> groups = groupService.getAll(pageNumber, pageSize);
        Page<GroupResponse> responseContent = ResultMapper.INSTANCE.toGroupsResponse(groups);

        response.status(200).send(responseContent);
    }
}
