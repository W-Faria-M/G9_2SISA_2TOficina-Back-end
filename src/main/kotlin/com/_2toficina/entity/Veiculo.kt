package com._2toficina.entity

import jakarta.persistence.*

@Entity
@Table(name = "veiculo")
data class Veiculo(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(nullable = false, length = 10)
    var placa: String = "",

    @JoinColumn(nullable = false, name = "fk_usuario")
    @ManyToOne
    var usuario: Usuario? = null,

    @Column(nullable = false, length = 45)
    var marca: String = "",

    @Column(nullable = false, length = 45)
    var modelo: String = "",

    @Column(nullable = false)
    var ano: Int = 0,

    @Column(nullable = false)
    var km : Double = 0.0,

)