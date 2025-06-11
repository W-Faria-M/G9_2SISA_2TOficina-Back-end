package com._2toficina.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "usuario")
data class Usuario(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @JoinColumn(nullable = false, name = "fk_tipo_usuario")
    @ManyToOne
    var tipoUsuario: TipoUsuario? = null,

    @Column(nullable = false, length = 45)
    var nome: String = "",

    @Column(nullable = false, length = 45)
    var sobrenome: String = "",

    @Column(nullable = false, length = 45)
    var telefone: String = "",

    @Column(nullable = false, length = 256)
    var email: String = "",

    @Column(nullable = false, length = 128)
    var senha: String = "",

    @Column(nullable = false, name = "data_cadastro")
    var dataCadastro: LocalDateTime = LocalDateTime.now(),

    @Column(length = 45)
    var sexo: String? = null,

    @Column(name = "data_nasc")
    var dataNascimento: LocalDate? = null
)