package com._2toficina.entity

import java.io.Serializable
import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class QuantidadeServicosPorMesId(
    @Column(name = "ano")
    var ano: Int = 0,
    @Column(name = "mes")
    var mes: Int = 0,
    @Column(name = "id_servico")
    var idServico: Int = 0
) : Serializable