package com._2toficina.dto

import java.time.LocalDate
import java.time.LocalDateTime

data class RespostaUsuario(
    val id: Int,
    val enderecoId: Int?,
    val tipo: TipoUsuarioEnum,
    val nome: String,
    val telefone: String?,
    val email: String,
    val dataCadastro: LocalDateTime,
    val sexo: String?,
    val dataNascimento: LocalDate?
)
