package com._2toficina.repository

import com._2toficina.entity.StatusServico
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface StatusServicoRepository: JpaRepository<StatusServico, Int> {}