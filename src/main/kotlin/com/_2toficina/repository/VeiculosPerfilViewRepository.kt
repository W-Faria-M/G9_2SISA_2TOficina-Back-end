package com._2toficina.repository

import com._2toficina.entity.VeiculosPerfilView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VeiculosPerfilViewRepository : JpaRepository<VeiculosPerfilView, Int> {
    fun findByUsuarioId(usuarioId: Int): List<VeiculosPerfilView>
}
