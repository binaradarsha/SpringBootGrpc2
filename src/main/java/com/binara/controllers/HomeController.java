package com.binara.controllers;

import com.binara.entities.User;
import com.binara.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class HomeController {

    @Autowired
    private UserService userService;

//    @GetMapping(value = "/")
//    public String index(){
//        return "Hello world";
//    }

    @GetMapping(value = "/")
    public User index() {
        return userService.getUser((long) 2);
    }

    @GetMapping(value = "/private")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String privateArea() {
        return "Private Area.....";
    }

}
