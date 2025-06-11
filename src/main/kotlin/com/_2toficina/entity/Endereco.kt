package com._2toficina.entity

import jakarta.persistence.*

@Entity
@Table(name = "endereco")
data class Endereco(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @JoinColumn(nullable = false, name = "fk_usuario")
    @OneToOne
    var usuario: Usuario? = null,

    @JoinColumn(nullable = false, name = "fk_logradouro")
    @ManyToOne
    var tipoLogradouro: Logradouro? = null,

    @Column(nullable = false, length = 45, name = "nome_logradouro")
    var nomeLogradouro: String = "",

    @Column(nullable = false, name = "numero_logradouro")
    var numeroLogradouro: Int = 0,

    @Column(nullable = false, length = 60)
    var cidade: String = "",

    @Column(nullable = false, length = 45)
    var estado: String = "",

    @Column(nullable = false, length = 45)
    var bairro: String = "",

    @Column(nullable = false, length = 8)
    var cep: String = "",

    @Column(nullable = false, length = 25)
    var complemento: String = "",

)