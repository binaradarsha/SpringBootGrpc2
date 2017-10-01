package com.binara.controller.grpc;


import com.binara.config.AuthServerInterceptor;
import com.binara.entities.Student;
import com.binara.services.StudentService;
import io.grpc.stub.StreamObserver;
import net.devh.springboot.autoconfigure.grpc.server.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;


/**
 * Created by binara on 7/8/17.
 */
@GrpcService(value = StudentGrpc.class, interceptors = {AuthServerInterceptor.class})
public class StudentController extends StudentGrpc.StudentImplBase {

    @Autowired
    private StudentService studentService;

    @Override
    @PreAuthorize("hasAuthority('USER')")
    public void getStudent(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
        Student student = studentService.getStudent(request.getId());
        StudentResponse response = StudentResponse.newBuilder()
                .setId(student.getId())
                .setName(student.getName())
                .setAge(student.getAge())
//                .setId(1)
//                .setName("test-name")
//                .setAge(43)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

}
