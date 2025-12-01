package com._2toficina.dto

data class QuantidadeServicosPorMesRes(
    val ano: Int,
    val mes: Int,
    val idServico: Int,
    val nomeServico: String,
    val totalServicos: Long
)