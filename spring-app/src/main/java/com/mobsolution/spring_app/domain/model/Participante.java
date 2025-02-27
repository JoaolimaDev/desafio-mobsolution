package com.mobsolution.spring_app.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "tb_participante")
@Data
public class Participante {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome",  nullable = false,  length = 255)
    @NotNull(message = "Por favor preencha campo nome.")
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    @NotNull(message = "Por favor preencha campo email.")
    private String email;


    @Column(name = "cpf", nullable = false, unique = true)
    @NotNull(message = "Por favor preencha campo cpf.")
    private String cpf;


    @NotNull(message = "Por favor preencha campo evento.")
    @ManyToOne
    @JoinColumn(name = "evento_id")
    private Evento evento;

    
}
