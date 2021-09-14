package com.schess.repositories;

import com.schess.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

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