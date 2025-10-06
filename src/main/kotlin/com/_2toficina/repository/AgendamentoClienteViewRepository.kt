package com._2toficina.repository

import com._2toficina.entity.AgendamentoClienteView
import org.springframework.data.jpa.repository.JpaRepository

interface AgendamentoClienteViewRepository: JpaRepository<AgendamentoClienteView, Int> {
    fun findByUsuarioId(usuarioId: Int): List<AgendamentoClienteView>
}