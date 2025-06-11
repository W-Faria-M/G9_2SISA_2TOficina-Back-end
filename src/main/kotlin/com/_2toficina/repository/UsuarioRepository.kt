package com._2toficina.repository

import com._2toficina.entity.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UsuarioRepository : JpaRepository<Usuario, Int> {

    fun findByTipoUsuarioId(tipoId: Int): List<Usuario>

    fun findByEmail(email: String): Usuario?

}