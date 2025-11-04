package com._2toficina.repository

import com._2toficina.entity.PerfilUsuarioView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PerfilUsuarioViewRepository : JpaRepository<PerfilUsuarioView, Int> {

    fun findByUsuarioId(usuarioId: Int): List<PerfilUsuarioView>

}
