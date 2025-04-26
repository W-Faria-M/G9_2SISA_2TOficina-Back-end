package com._2toficina.entity

import jakarta.persistence.*

@Entity
@Table(name = "veiculo")
data class Veiculo(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @field:Column(nullable = false, length = 10)
    var placa: String = "",

    @field:Column(nullable = false)
    var fkUsuario: Int = 0,

    @field:Column(nullable = false, length = 45)
    var marca: String = "",

    @field:Column(nullable = false, length = 45)
    var modelo: String = "",

    @field:Column(nullable = false)
    var ano: Int = 0,

    @field:Column(nullable = false)
    var km : Double = 0.0,
)