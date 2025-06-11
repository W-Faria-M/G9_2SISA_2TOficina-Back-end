package com._2toficina.entity

import jakarta.persistence.*

@Entity
@Table(name = "servico")
data class Servico(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @JoinColumn(nullable = false, name = "fk_categoria_servico")
    @ManyToOne
    var categoriaServico: CategoriaServico? = null,

    @Column(nullable = false, length = 45)
    var nome: String = "",

    @Column(nullable = false, length = 128)
    var descricao: String = "",

    @Column(nullable = false, name = "eh_rapido")
    var ehRapido: Boolean = false,

)