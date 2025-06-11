package com._2toficina.repository

import com._2toficina.entity.TipoUsuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TipoUsuarioRepository: JpaRepository<TipoUsuario, Int> {

    fun findByTipoIgnoreCase(tipo: String): TipoUsuario?

    fun existsByTipoIgnoreCase(tipo: String): Boolean

}