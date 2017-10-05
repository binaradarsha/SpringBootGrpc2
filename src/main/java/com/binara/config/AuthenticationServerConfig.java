package com.binara.config;

import com.binara.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class AuthenticationServerConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    /**
     * Password grants are switched on by injecting an AuthenticationManager.
     * Here, we setup the builder so that the userDetailsService is the one we coded.
     *
     * @param builder
     * @param repository
     * @throws Exception
     */
    @Autowired
    public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repository) throws Exception {
//		builder.userDetailsService(userDetailsService(repository)).passwordEncoder(passwordEncoder);
        builder.userDetailsService(userDetailsService(repository));
    }

    /**
     * We return an instance of our CustomUserDetails.
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
    }

}
