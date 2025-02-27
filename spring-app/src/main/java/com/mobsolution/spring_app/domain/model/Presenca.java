package com.mobsolution.spring_app.domain.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Entity
@Table(name = "tb_presenca", 
    uniqueConstraints = @UniqueConstraint(columnNames = {"participante_id", "data"}))
@Data
public class Presenca {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "Por favor preencha campo data.")
    @Column(name = "data", nullable = false)
    private LocalDate data;

    @NotNull(message = "Por favor preencha campo participante.")
    @ManyToOne
    @JoinColumn(name = "participante_id")
    private Participante participante;
}
