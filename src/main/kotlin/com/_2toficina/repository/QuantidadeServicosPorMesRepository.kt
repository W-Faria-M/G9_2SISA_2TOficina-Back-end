package com._2toficina.repository

import com._2toficina.entity.QuantidadeServicosPorMesId
import com._2toficina.entity.QuantidadeServicosPorMesView
import org.springframework.data.jpa.repository.JpaRepository

interface QuantidadeServicosPorMesRepository :
    JpaRepository<QuantidadeServicosPorMesView, QuantidadeServicosPorMesId> {

    fun findByIdAno(ano: Int): List<QuantidadeServicosPorMesView>
    fun findByIdAnoAndIdIdServico(ano: Int, idServico: Int): List<QuantidadeServicosPorMesView>
}