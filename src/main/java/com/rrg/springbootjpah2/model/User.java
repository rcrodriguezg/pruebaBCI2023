package com.rrg.springbootjpah2.model;



import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @Column(name = "id", unique = true, updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @NotBlank(message = "User Name")
    @Column(name = "name", nullable = false)
    @NotNull
    private String name;
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "A valid mail address is mandatory")
    @NotNull
    private String email;
    @Column(name = "password", nullable = false)
    @NotNull
    private String password;
    @Column(name = "phones")
    private String phones;

    @Column(name = "created")
    private Date created;

    @Column(name = "modified")
    private Date modified;
    @Column(name = "last_login")
    private Date lastLogin;

    @Column(name = "isactive")
    private Boolean isActive;
}


