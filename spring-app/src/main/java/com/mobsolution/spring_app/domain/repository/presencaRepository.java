package com.mobsolution.spring_app.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mobsolution.spring_app.domain.model.presenca;


@Repository
@RepositoryRestResource(collectionResourceRel = "presenca", path = "presenca")
public interface presencaRepository extends JpaRepository<presenca, Long>{
    

    @Override
    @RestResource(exported = false)
    <S extends presenca> S save(S entity);

}
