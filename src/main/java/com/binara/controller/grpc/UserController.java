package com.binara.controller.grpc;

import com.binara.entities.User;
import com.binara.services.UserService;
import io.grpc.stub.StreamObserver;
//import net.devh.springboot.autoconfigure.grpc.server.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

//@GrpcService(UserGrpc.class)
public class UserController extends UserGrpc.UserImplBase {

    @Autowired
    private UserService userService;

    @Override
    public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        System.out.println("))) User Controller...");
        User user = userService.getUser(request.getId());
//        User user = new User("abc", "123", Arrays.asList(new Role("USER")));

        UserResponse response = UserResponse.newBuilder()
//                .setId(34)
//                .setUsername("Binara")
//                .setPassword("123")
                .setId(user.getId())
                .setUsername(user.getUsername())
                .setPassword(user.getPassword())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
