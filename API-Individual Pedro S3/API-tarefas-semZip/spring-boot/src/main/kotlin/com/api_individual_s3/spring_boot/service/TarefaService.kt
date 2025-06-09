package com.api_individual_s3.spring_boot.service

import com.api_individual_s3.spring_boot.entity.Tarefa
import com.api_individual_s3.spring_boot.repository.TarefaRep
import org.springframework.stereotype.Service

@Service
class TarefaService(private val tarefaRep: TarefaRep) {

    fun save(tarefa: Tarefa): Tarefa = tarefaRep.save(tarefa)

    fun findAll(): List<Tarefa> = tarefaRep.findAll()

    fun findByTituloIgnoreCaseContaining(titulo: String): List<Tarefa> =
        tarefaRep.findByTituloIgnoreCaseContaining(titulo)

    fun findById(id: Int) = tarefaRep.findById(id)

    fun existsById(id: Int) = tarefaRep.existsById(id)

    fun deleteById(id: Int) = tarefaRep.deleteById(id)
}