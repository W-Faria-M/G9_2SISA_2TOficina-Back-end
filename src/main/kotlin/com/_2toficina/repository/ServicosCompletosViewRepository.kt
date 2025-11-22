package com._2toficina.repository

import com._2toficina.entity.ServicosCompletosView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ServicosCompletosViewRepository : JpaRepository<ServicosCompletosView, Int> {}
