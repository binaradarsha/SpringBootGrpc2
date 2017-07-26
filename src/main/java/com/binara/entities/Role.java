package com.binara.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    String name;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role", catalog = "vanilladb", joinColumns = {
            @JoinColumn(name = "role_id", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "user_id",
                    nullable = false, updatable = false) })
    private List<User> users;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
