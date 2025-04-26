package com._2toficina.repository

import com._2toficina.entity.Veiculo
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface VeiculoRepository: JpaRepository<Veiculo, Int> {
    fun findByFkUsuario(fkUsuario: Int): List<Veiculo>
}