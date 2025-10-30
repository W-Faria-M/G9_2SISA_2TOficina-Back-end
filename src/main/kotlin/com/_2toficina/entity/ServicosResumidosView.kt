package com._2toficina.entity

import jakarta.persistence.*
import org.hibernate.annotations.Immutable

@Entity
@Immutable
@Table(name = "vw_servicos_resumidos")
data class ServicosResumidosView(
    @Id
    @Column(name = "id_servico")
    val servicoId: Int?,

    @Column(name = "nome_servico")
    val nomeServico: String?
) {
    // construtor vazio exigido pelo Hibernate
    constructor() : this(
        servicoId = null,
        nomeServico = null,
    )
}