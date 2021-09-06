package com.schess.repositories;

import com.schess.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    void delete(User users);

    @Override
    void deleteAll();

    @Override
    void deleteAll(Iterable<? extends User> users);

    @Override
    void deleteById(Long aLong);

    User getByUsername(String username);

    User getByActivationCode(String activationCode);

}