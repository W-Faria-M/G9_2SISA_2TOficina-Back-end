package com._2toficina.repository

import com._2toficina.entity.Funcionamento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FuncionamentoRepository: JpaRepository<Funcionamento, Int> {

    fun findByDiaSemana(diaSemana: Int): Funcionamento?

}