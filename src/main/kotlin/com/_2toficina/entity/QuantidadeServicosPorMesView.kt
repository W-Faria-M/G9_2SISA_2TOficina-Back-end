package com._2toficina.entity

import jakarta.persistence.*
import org.hibernate.annotations.Immutable

@Entity
@Table(name = "vw_quantidade_servicos_por_mes")
@Immutable
data class QuantidadeServicosPorMesView(
    @EmbeddedId
    var id: QuantidadeServicosPorMesId = QuantidadeServicosPorMesId(),

    @Column(name = "nome_servico")
    var nomeServico: String = "",

    @Column(name = "total_servicos")
    var totalServicos: Long = 0
)