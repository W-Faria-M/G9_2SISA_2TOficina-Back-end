package com.api_individual_s3.spring_boot.dto

import jakarta.persistence.Id

data class TarefaResponse(
    val id: Int?,
    var titulo: String,
    var descricao: String,
    var concluida: Boolean
)
