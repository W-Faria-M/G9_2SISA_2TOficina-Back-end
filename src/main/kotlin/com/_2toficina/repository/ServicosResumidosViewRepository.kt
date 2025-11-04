package com._2toficina.repository

import com._2toficina.entity.ServicosResumidosView
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ServicosResumidosViewRepository : JpaRepository<ServicosResumidosView, Int> {

}
