package com.mobsolution.spring_app.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.mobsolution.spring_app.domain.model.User;


@Repository
@RepositoryRestResource(exported = false)
public interface UserRepository extends JpaRepository<User, Long>{
    

    Optional<User> findByUsername(String username);
}