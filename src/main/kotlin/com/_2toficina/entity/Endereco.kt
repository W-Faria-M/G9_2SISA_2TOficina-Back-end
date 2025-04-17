package com._2toficina.entity

import jakarta.persistence.*

@Entity
@Table(name = "endereco")
data class Endereco(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(nullable = false)
    var logradouro: String = ""
)

