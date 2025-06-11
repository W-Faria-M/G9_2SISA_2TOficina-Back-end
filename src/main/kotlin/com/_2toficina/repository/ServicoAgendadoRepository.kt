package com._2toficina.repository

import com._2toficina.entity.ServicoAgendado
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ServicoAgendadoRepository: JpaRepository <ServicoAgendado, Int> {

    fun findByAgendamentoId(Id: Int): List<ServicoAgendado>

}