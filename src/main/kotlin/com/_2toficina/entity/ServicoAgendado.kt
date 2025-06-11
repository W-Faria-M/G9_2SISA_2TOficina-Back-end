package com._2toficina.entity

import jakarta.persistence.*

@Entity
@Table(name = "servico_agendado")
data class ServicoAgendado(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @JoinColumn(nullable = false, name = "fk_agendamento")
    @ManyToOne
    var agendamento: Agendamento? = null,

    @JoinColumn(nullable = false, name = "fk_servico")
    @ManyToOne
    var servico: Servico? = null,

)