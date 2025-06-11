package com._2toficina.repository

import com._2toficina.entity.Servico
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ServicoRepository: JpaRepository<Servico, Int> {

    fun findByCategoriaServicoId(categoriaId: Int?): List<Servico>

    fun findByEhRapido(ehRapido: Boolean): List<Servico>

}