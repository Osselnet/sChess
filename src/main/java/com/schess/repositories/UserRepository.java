package com.schess.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.schess.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Override
    void delete(User user);

    @Override
    void deleteAll();

    @Override
    void deleteAll(Iterable<? extends User> users);

    @Override
    void deleteById(Long aLong);

    User findByEmail(@Param("email") String email);

    User findByName(@Param("name") String name);
}