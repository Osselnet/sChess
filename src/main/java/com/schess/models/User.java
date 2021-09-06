package com.schess.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

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

    public User() {
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.role = role;
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHash = DigestUtils.sha1Hex(password + passwordSalt);
        this.activationCode = RandomStringUtils.randomAlphanumeric(32);
    }

    public boolean checkPassword(String password) {
        return DigestUtils.sha1Hex(password + passwordSalt).equals(passwordHash);
    }

}
