package com._2toficina.entity

import jakarta.persistence.*

@Entity
@Table(name = "logradouro")
data class Logradouro(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(nullable = false, length = 25)
    var tipo: String = "",

)