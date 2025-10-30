package com._2toficina.repository

import com._2toficina.entity.VeiculosClienteView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VeiculosClienteViewRepository : JpaRepository<VeiculosClienteView, Int> {
    fun findByUsuarioId(usuarioId: Int): List<VeiculosClienteView>
}
