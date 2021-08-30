package com.schess.service;

import com.schess.models.Users;
import com.schess.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<Users> findAll(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return userRepository.findAll();
        } else {
            return (List<Users>) userRepository.findByName(stringFilter);
        }
    }

    public Users findByName(String name) {
        return userRepository.findByName(name);
    }

    public Optional<Users> findById(Long id) {
        return userRepository.findById(id);
    }

    public void delete(Users users) {
        userRepository.delete(users);
    }

    public void save(Users users) {
        userRepository.save(users);
    }
}
