package com.mobsolution.spring_app.controller;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobsolution.spring_app.domain.dto.presencaDTO;
import com.mobsolution.spring_app.domain.model.presenca;
import com.mobsolution.spring_app.service.presencaService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/presenca")
@Tag(name = "presenca-entity-controller")
public class presencaController {
    

    public final presencaService presencaService;

    @PostMapping("/cadastro")
    public EntityModel<presenca> postPresenca(@RequestBody presencaDTO requestBody){

        presenca presenca = presencaService.postPresenca(requestBody);
        EntityModel<presenca> resource = EntityModel.of(presenca);

        resource.add(linkTo(presencaController.class).slash(presenca.getId())
        .withSelfRel());

        return resource;
    }

    @PutMapping("/atualiza/{id}")
    public EntityModel<presenca> putPresenca(@PathVariable String id, @RequestBody presencaDTO requestBody){

        presenca presenca = presencaService.putPresenca(id, requestBody);

        EntityModel<presenca> resource = EntityModel.of(presenca);

        resource.add(linkTo(presencaController.class).slash(presenca.getId())
        .withSelfRel());

        return resource;

    }
}
