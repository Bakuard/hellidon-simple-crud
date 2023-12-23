package com.bakuard.simpleCrud.dto;

import com.bakuard.simpleCrud.dal.Page;
import com.bakuard.simpleCrud.model.Group;
import com.bakuard.simpleCrud.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.function.Function;

@Mapper(imports = LocalDate.class)
public interface ResultMapper {

    ResultMapper INSTANCE = Mappers.getMapper(ResultMapper.class);

    @Mapping(target = "group", expression = "java(groupMapper.apply(request.getGroupId()))")
    @Mapping(target = "birthday", expression = "java(LocalDate.parse(request.getBirthday()))")
    Student toStudent(NewStudentRequest request, Function<Long, Group> groupMapper);

    Group toGroup(NewGroupRequest request);

    GroupResponse toGroupResponse(Group group);

    @Mapping(target = "birthday", expression = "java(student.getBirthday().toString())")
    StudentListItemResponse toStudentListItemResponse(Student student);

    default Page<StudentListItemResponse> toStudentsListItemResponse(Page<Student> page) {
        return page.map(this::toStudentListItemResponse);
    }

    default Page<GroupResponse> toGroupsResponse(Page<Group> page) {
        return page.map(this::toGroupResponse);
    }
}
