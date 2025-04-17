package com._2toficina.entity

import com._2toficina.TipoUsuarioEnum
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime

@Entity
@Table(name = "usuario")
data class Usuario(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @ManyToOne
    @JoinColumn(name = "fkEndereco")
    var endereco: Endereco? = null,

    @Enumerated(EnumType.STRING)
    @Column(name = "fkTipoUsuario", nullable = false)
    var tipo: TipoUsuarioEnum = TipoUsuarioEnum.CLIENTE,

    @Column(nullable = false, length = 45)
    var nome: String = "",

    @Column(length = 45)
    var telefone: String? = null,

    @Column(nullable = false, length = 256)
    var email: String = "",

    @Column(nullable = false, length = 128)
    var senha: String = "",

    @Column(name = "data_cadastro")
    var dataCadastro: LocalDateTime = LocalDateTime.now(),

    @Column(length = 45)
    var sexo: String? = null,

    @Column(name = "data_nasc")
    var dataNascimento: LocalDate? = null
)

