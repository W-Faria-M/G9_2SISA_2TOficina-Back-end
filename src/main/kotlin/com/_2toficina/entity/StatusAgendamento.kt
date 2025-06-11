package com._2toficina.entity

import jakarta.persistence.*

@Entity
@Table(name = "status_agendamento")
data class StatusAgendamento(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(nullable = false, length = 45)
    var status: String = ""

)