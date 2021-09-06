package com.schess.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class User extends AbstractEntity {
    private String username;
    private String passwordSalt;
    private String passwordHash;
    private Role role;
    private String activationCode;
    private boolean active;
 }
