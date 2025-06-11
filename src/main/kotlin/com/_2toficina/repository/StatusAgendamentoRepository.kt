package com._2toficina.repository

import com._2toficina.entity.StatusAgendamento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StatusAgendamentoRepository: JpaRepository<StatusAgendamento, Int> {

    fun findStatusById(id: Int): StatusAgendamento?

}