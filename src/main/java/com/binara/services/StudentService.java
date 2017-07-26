package com.binara.services;

import com.binara.entities.Student;
import com.binara.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    public Student getStudent(long id){
        return repository.findOne(id);
    }
}
