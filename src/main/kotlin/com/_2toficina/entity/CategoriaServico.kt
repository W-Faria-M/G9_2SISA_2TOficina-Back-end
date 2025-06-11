package com._2toficina.entity

import jakarta.persistence.*

@Entity
@Table(name = "categoria_servico")
data class CategoriaServico(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(nullable = false, length = 45)
    var nome: String = "",

)