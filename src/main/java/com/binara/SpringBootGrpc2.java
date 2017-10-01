package com.binara;

import com.binara.config.CustomUserDetails;
import com.binara.controller.grpc.StudentController;
import com.binara.entities.Role;
import com.binara.entities.User;
import com.binara.repositories.UserRepository;
import com.binara.services.RoleService;
import com.binara.services.UserService;
import io.grpc.ServerBuilder;
import io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.NettyServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.File;
import java.util.Arrays;


@SpringBootApplication
public class SpringBootGrpc2 {

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootGrpc2.class, args);
//        startGrpcServer(true);
    }

    /**
     * Password grants are switched on by injecting an AuthenticationManager.
     * Here, we setup the builder so that the userDetailsService is the one we coded.
     *
     * @param builder
     * @param repository
     * @throws Exception
     */
    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repository, UserService service, RoleService rs) throws Exception {
        //Setup a default user if db is empty
        if (repository.count() == 0) {
//			service.save(new User("user", "user", Arrays.asList(new Role("USER"), new Role("ACTUATOR"))));
            Role role = new Role("USER");
            rs.save(role);
            service.save(new User("abc", "123", Arrays.asList(rs.findByName("USER"))));
        }
//		builder.userDetailsService(userDetailsService(repository)).passwordEncoder(passwordEncoder);
        builder.userDetailsService(userDetailsService(repository));
    }

    /**
     * We return an istance of our CustomUserDetails.
     *
     * @param repository
     * @return
     */
    @Bean
    public UserDetailsService userDetailsService(final UserRepository repository) {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return new CustomUserDetails(repository.findByUsername(username));
            }
        };
//        return username -> new CustomUserDetails(repository.findByUsername(username));
    }

    private static void startGrpcServer(boolean secured) {
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
