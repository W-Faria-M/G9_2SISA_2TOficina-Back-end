package com._2toficina.dto

data class EnderecoRes(
    val usuarioId: Int,
    val tipoLogradouro: String,
    val nomeLogradouro: String,
    val numeroLogradouro: Int,
    val cidade: String,
    val estado: String,
    val bairro: String,
    val cep: String,
    val complemento: String? = null
)
