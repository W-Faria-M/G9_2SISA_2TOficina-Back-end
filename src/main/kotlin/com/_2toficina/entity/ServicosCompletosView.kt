package com._2toficina.entity

import jakarta.persistence.*
import org.hibernate.annotations.Immutable

@Entity
@Immutable
@Table(name = "vw_servicos_completos")
data class ServicosCompletosView(
    @Id
    @Column(name = "id_servico")
    val servicoId: Int?,

    @Column(name = "nome_servico")
    val nomeServico: String?,

    @Column(name = "nome_categoria")
    val nomeCategoria: String?,

    @Column
    val descricao: String?,

    @Column(name = "eh_rapido")
    val ehRapido: Boolean?,

    @Column(name = "status_atual")
    val status: String?

) {
    // construtor vazio exigido pelo Hibernate
    constructor() : this(
        servicoId = null,
        nomeServico = null,
        nomeCategoria = null,
        descricao = null,
        ehRapido = null,
        status = null
    )
}
