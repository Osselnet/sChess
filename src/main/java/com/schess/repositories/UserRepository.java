package com.schess.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.schess.models.User;

import javax.persistence.NamedStoredProcedureQuery;

@CrossOrigin
public interface UserRepository extends CrudRepository<User, Long> {

    @Override
    void delete(User entity);

    @Override
    void deleteAll();

    @Override
    void deleteAll(Iterable<? extends User> entities);

    @Override
    void deleteById(Long aLong);

    User findByEmail(@Param("email") String email);

    User findByName(@Param("name") String name);
}