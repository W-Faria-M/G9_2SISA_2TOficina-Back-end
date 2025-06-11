package com._2toficina.repository

import com._2toficina.entity.Excecao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
interface ExcecaoRepository: JpaRepository<Excecao, Int> {

    fun existsByDataInicioGreaterThanEqualAndDataFimLessThanEqual(dataInicio: LocalDate, dataFim: LocalDate): Boolean

}