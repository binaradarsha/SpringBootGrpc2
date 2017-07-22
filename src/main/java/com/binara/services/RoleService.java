package com.binara.services;

import com.binara.entities.Role;
import com.binara.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    public void save(Role role) {
        repository.save(role);
    }

    public Role findByName(String name) {
        return repository.findByName(name);
    }

}
