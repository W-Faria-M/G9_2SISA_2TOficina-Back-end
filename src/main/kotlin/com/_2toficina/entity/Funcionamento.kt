package com._2toficina.entity

import jakarta.persistence.*
import java.time.LocalTime

@Entity
@Table(name = "funcionamento")
data class Funcionamento(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(nullable = false, name = "dia_semana")
    var diaSemana: Int = 0,

    @Column(nullable = false, name = "inicio_funcionamento")
    var inicioFuncionamento: LocalTime? = null,

    @Column(nullable = false, name = "fim_funcionamento")
    var fimFuncionamento: LocalTime? = null

)
