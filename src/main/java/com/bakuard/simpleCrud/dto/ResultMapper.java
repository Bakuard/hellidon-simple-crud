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

    @Mapping(target = "group",
            expression = "java(request.getGroupId() != null ? groupMapper.apply(request.getGroupId()) : null)")
    @Mapping(target = "birthday", expression = "java(LocalDate.parse(request.getBirthday()))")
    Student toStudent(NewStudentRequest request, Function<Long, Group> groupMapper);

    Group toGroup(NewGroupRequest request);

    GroupResponse toGroupResponse(Group group);

    @Mapping(target = "birthday", expression = "java(student.getBirthday().toString())")
    StudentResponse toStudentResponse(Student student);

    default Page<StudentResponse> toStudentsResponse(Page<Student> page) {
        return page.map(this::toStudentResponse);
    }

    default Page<GroupResponse> toGroupsResponse(Page<Group> page) {
        return page.map(this::toGroupResponse);
    }
}
