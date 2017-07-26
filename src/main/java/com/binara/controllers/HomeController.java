package com.binara.controllers;

import com.binara.entities.User;
import com.binara.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @GetMapping(value = "/")
    public String index(){
        return "Hello world";
    }

    @GetMapping(value = "/user")
    public ResponseEntity<User> user() {
        User user = userService.getUser((long) 2);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping(value = "/private")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String privateArea() {
        return "Private Area.....";
    }

    @GetMapping(value = "/auth_user")
    public ResponseEntity<UserDetails> authUser() {
        return new ResponseEntity<UserDetails>(userDetailsService.loadUserByUsername("abc"), HttpStatus.OK);
    }
}
