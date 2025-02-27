package com.mobsolution.spring_app.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;

import com.mobsolution.spring_app.domain.model.Participante;
import com.mobsolution.spring_app.domain.model.Presenca;

import io.swagger.v3.oas.annotations.tags.Tag;


@Repository
@Tag(name = "presenca-entity-controller")
@RepositoryRestResource(collectionResourceRel = "presenca", path = "presenca")
public interface presencaRepository extends JpaRepository<Presenca, Long>{
    

    @Override
    @RestResource(exported = false)
    <S extends Presenca> S save(S entity);

    
    List<Presenca> findAllByParticipante(Participante participante_id);

}
