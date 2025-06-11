package com._2toficina.repository

import com._2toficina.entity.CategoriaServico
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoriaServicoRepository: JpaRepository<CategoriaServico, Int> {

    fun findByNomeIgnoreCase(nome: String): List<CategoriaServico>

}