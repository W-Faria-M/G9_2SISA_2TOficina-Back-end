package com._2toficina.dto

import java.time.LocalDate
import java.time.LocalTime

data class AgendamentoRes(
    val nome: String,
    val sobrenome: String,
    val data: LocalDate,
    val hora: LocalTime,
    val horaRetirada: String?,
    val descricao: String,
    val statusAgendamento: String,
    val observacao: String?
)
