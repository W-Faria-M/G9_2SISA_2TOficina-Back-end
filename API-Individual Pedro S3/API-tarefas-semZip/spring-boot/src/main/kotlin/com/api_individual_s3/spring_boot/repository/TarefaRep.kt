package com.api_individual_s3.spring_boot.repository

import com.api_individual_s3.spring_boot.entity.Tarefa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TarefaRep : JpaRepository<Tarefa, Int> {
    fun findByTituloIgnoreCaseContaining(titulo: String): List<Tarefa>
}