package com._2toficina.entity

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "excecao")
data class Excecao(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(nullable = false, name = "data_inicio")
    var dataInicio: LocalDate? = null,

    @Column(nullable = false, name = "data_fim")
    var dataFim: LocalDate? = null,

    @Column(nullable = false, name = "inicio_excecao")
    var inicioExcecao: String = "",

    @Column(nullable = false, name = "fim_excecao")
    var fimExcecao: String = "",

)
