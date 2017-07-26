package com.binara.controller.grpc;


import com.binara.entities.Student;
import com.binara.repositories.StudentRepository;
import com.binara.services.StudentService;
import io.grpc.stub.StreamObserver;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Created by binara on 7/8/17.
 */
@GrpcService(StudentGrpc.class)
public class StudentController extends StudentGrpc.StudentImplBase {

    @Autowired
    private StudentService studentService;

    @Override
    public void getStudent(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        Student student = studentService.getStudent(request.getId());
        StudentResponse response = StudentResponse.newBuilder()
                .setId(student.getId())
                .setName(student.getName())
                .setAge(student.getAge())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

}
