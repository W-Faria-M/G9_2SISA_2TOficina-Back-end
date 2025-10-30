package com._2toficina.entity

import jakarta.persistence.*
import org.hibernate.annotations.Immutable

@Entity
@Immutable
@Table(name = "vw_perfil_usuario")
data class PerfilUsuarioView(
    @Id
    @Column(name = "id_usuario")
    val usuarioId: Int? = null,

    @Column(name = "nome_completo")
    val nomeCompleto: String? = null,

    @Column(name = "email")
    val email: String? = null,

    @Column(name = "telefone")
    val telefone: String? = null,

    @Column(name = "id_veiculo")
    val idVeiculo: Int? = null,

    @Column(name = "marca")
    val marca: String? = null,

    @Column(name = "modelo")
    val modelo: String? = null,

    @Column(name = "ano")
    val ano: Int? = null,

    @Column(name = "km")
    val km: Int? = null,

    @Column(name = "placa")
    val placa: String? = null,

    @Column(name = "qtd_pendentes")
    val qtdPendentes: Int? = null,

    @Column(name = "qtd_concluidos")
    val qtdConcluidos: Int? = null
)
{
    // construtor vazio exigido pelo Hibernate
    constructor() : this(
        usuarioId = null,
        nomeCompleto = null,
        email = null,
        telefone = null,
        idVeiculo = null,
        marca = null,
        modelo = null,
        ano = null,
        km = null,
        placa = null,
        qtdPendentes = null,
        qtdConcluidos = null
    )
}