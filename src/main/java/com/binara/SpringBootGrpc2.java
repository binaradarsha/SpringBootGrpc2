package com.binara;

import com.binara.config.CustomUserDetails;
import com.binara.entities.Role;
import com.binara.entities.User;
import com.binara.repositories.UserRepository;
import com.binara.services.RoleService;
import com.binara.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;


@SpringBootApplication
public class SpringBootGrpc2 {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootGrpc2.class, args);
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
    private UserDetailsService userDetailsService(final UserRepository repository) {
        return username -> new CustomUserDetails(repository.findByUsername(username));
    }

}
