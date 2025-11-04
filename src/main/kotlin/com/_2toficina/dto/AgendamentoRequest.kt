package com._2toficina.dto

import java.time.LocalDate
import java.time.LocalTime

data class AgendamentoRequest(
    val usuarioId: Int,
    val data: LocalDate,
    val hora: LocalTime,
    val veiculo: String,
    val descricao: String,
    val observacao: String?,
    val servicosIds: List<Int>
)
