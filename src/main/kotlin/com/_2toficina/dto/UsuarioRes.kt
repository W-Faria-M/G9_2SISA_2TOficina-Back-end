package com._2toficina.dto

data class UsuarioRes(
    val id: Int,
    val nomeCompleto: String,
    val tipoUsuario: String,
    val telefone: String?,
    val email: String,
)
