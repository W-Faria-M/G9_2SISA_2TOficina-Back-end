package com._2toficina.repository

import com._2toficina.entity.Agendamento
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalTime

@Repository
interface AgendamentoRepository: JpaRepository<Agendamento, Int> {

    fun findByUsuarioId(usuarioId: Int): List<Agendamento>

    fun findByData(Data: LocalDate): List<Agendamento>

    fun findByStatusAgendamentoId(statusAgendamentoId: Int): List<Agendamento>

    fun findStatusAgendamentoById(id: Int): Agendamento?
    
//    fun deleteByFkAgendamento(id: Int)

    fun existsByDataAndHora(data: LocalDate, hora: LocalTime): Boolean

}