package com.bakuard.simpleCrud.controller;

import com.bakuard.simpleCrud.dal.Page;
import com.bakuard.simpleCrud.dto.NewStudentRequest;
import com.bakuard.simpleCrud.dto.ResultMapper;
import com.bakuard.simpleCrud.dto.StudentResponse;
import com.bakuard.simpleCrud.model.Student;
import com.bakuard.simpleCrud.service.GroupService;
import com.bakuard.simpleCrud.service.StudentService;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

public class StudentController implements HttpService {

    private final StudentService studentService;
    private final GroupService groupService;

    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @Override
    public void routing(HttpRules httpRules) {
        httpRules.get("/{id}", this::getById)
                .get("/", this::getAll)
                .post("/new", this::add)
                .delete("/delete/{id}", this::deleteById);
    }

    private void add(ServerRequest request, ServerResponse response) {
        NewStudentRequest studentRequest = request.content().as(NewStudentRequest.class);

        Student newStudent = ResultMapper.INSTANCE.toStudent(studentRequest, groupService::tryGetById);
        Student savedStudent = studentService.add(newStudent);
        StudentResponse studentResponse = ResultMapper.INSTANCE.toStudentResponse(savedStudent);

        response.status(200).send(studentResponse);
    }

    private void getAll(ServerRequest request, ServerResponse response) {
        int pageNumber = Integer.parseInt(request.query().get("pageNumber"));
        int pageSize = Integer.parseInt(request.query().get("pageSize"));

        Page<Student> students = studentService.getAll(pageNumber, pageSize);
        Page<StudentResponse> responseContent = ResultMapper.INSTANCE.toStudentsResponse(students);

        response.status(200).send(responseContent);
    }

    private void getById(ServerRequest request, ServerResponse response) {
        long studentId = Long.parseLong(
                request.path().pathParameters().get("id")
        );

        Student student = studentService.tryGetById(studentId);
        StudentResponse studentResponse = ResultMapper.INSTANCE.toStudentResponse(student);

        response.status(200).send(studentResponse);
    }

    private void deleteById(ServerRequest request, ServerResponse response) {
        long studentId = Long.parseLong(
                request.path().pathParameters().get("id")
        );

        studentService.tryDeleteById(studentId);

        response.status(200).send("Student with id=%d was deleted successfully".formatted(studentId));
    }
}
