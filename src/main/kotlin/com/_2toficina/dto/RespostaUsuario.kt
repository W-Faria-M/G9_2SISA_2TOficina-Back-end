package com._2toficina.dto

data class RespostaUsuario(
    val id: Int,
    val tipoUsuario: Int,
    val nomeCompleto: String,
    val telefone: String?,
    val email: String,
)
