package com.mobsolution.spring_app.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.mobsolution.spring_app.domain.model.evento;

@Repository
@RepositoryRestResource(collectionResourceRel = "evento", path = "evento")
public interface eventoRepository extends JpaRepository<evento, Long>{
    
}
