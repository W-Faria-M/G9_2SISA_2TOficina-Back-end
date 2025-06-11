package com._2toficina.repository

import com._2toficina.entity.Endereco
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface EnderecoRepository: JpaRepository<Endereco, Int> {

    fun findByUsuarioId(usuarioId: Int): Endereco

    fun existsByUsuarioId (usuarioId: Int): Boolean

    fun deleteByUsuarioId(usuarioId: Int)

}