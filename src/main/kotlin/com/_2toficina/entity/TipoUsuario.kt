package com._2toficina.entity

import jakarta.persistence.*

@Entity
@Table(name = "tipo_usuario")
data class TipoUsuario(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(nullable = false, length = 45)
    var tipo: String = "",

)