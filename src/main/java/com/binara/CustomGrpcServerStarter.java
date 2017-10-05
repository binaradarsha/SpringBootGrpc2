package com.binara;

import com.binara.controller.grpc.StudentController;
import io.grpc.ServerBuilder;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyServerBuilder;

import java.io.File;

public class CustomGrpcServerStarter {

    public static void startGrpcServer(boolean secured) {
        try {
            if (secured) {
                NettyServerBuilder.forPort(9095)
                        .sslContext(GrpcSslContexts.forServer(
                                new File("src/main/resources/localhost-cert.crt"),
                                new File("src/main/resources/localhost-primarykey.key")
                        ).build())
                        .addService(new StudentController())
                        .build()
                        .start()
                        .awaitTermination();

//                ServerBuilder.forPort(9095)
//                        .useTransportSecurity(
//                                new File("src/main/resources/localhost-cert.crt"),
//                                new File("src/main/resources/localhost-primarykey.key")
//                        )
//                        .addService(new StudentController())
//                        .build()
//                        .start()
//                        .awaitTermination();
            } else {
                ServerBuilder.forPort(9090)
                        .addService(new StudentController())
                        .build()
                        .start()
                        .awaitTermination();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.printf(ex.getMessage());
        }
    }

}
