package com.schess.repositories;

import com.schess.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Users, Long> {

    @Override
    void delete(Users users);

    @Override
    void deleteAll();

    @Override
    void deleteAll(Iterable<? extends Users> users);

    @Override
    void deleteById(Long aLong);

    Users findByEmail(@Param("email") String email);

    Users findByName(@Param("name") String name);
}