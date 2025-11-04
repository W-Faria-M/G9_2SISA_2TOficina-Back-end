package com._2toficina.repository

import com._2toficina.entity.ServicoAgendado
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
interface ServicoAgendadoRepository: JpaRepository <ServicoAgendado, Int> {

    fun findByAgendamentoId(Id: Int): List<ServicoAgendado>

    @Transactional
    @Modifying
    fun deleteByAgendamentoId(agendamentoId: Int)

}