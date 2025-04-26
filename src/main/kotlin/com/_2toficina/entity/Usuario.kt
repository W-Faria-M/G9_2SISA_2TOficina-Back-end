package com._2toficina.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "usuario")
data class Usuario(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @field:Column
    var fkEndereco: Int? = null,

    @field:Column(nullable = false)
    var fkTipoUsuario: Int = 0,

    @field:Column(nullable = false, length = 45)
    var nome: String = "",

    @field:Column(nullable = false, length = 45)
    var sobrenome: String = "",

    @field:Column(nullable = false, length = 45)
    var telefone: String = "",

    @field:Column(nullable = false, length = 256)
    var email: String = "",

    @field:Column(nullable = false, length = 128)
    var senha: String = "",

    @field:Column(nullable = false, name = "data_cadastro")
    var dataCadastro: LocalDateTime = LocalDateTime.now(),

    @field:Column(length = 45)
    var sexo: String? = null,

    @field:Column(name = "data_nasc")
    var dataNascimento: LocalDate? = null
)