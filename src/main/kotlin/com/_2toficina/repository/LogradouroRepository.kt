package com._2toficina.repository

import com._2toficina.entity.Logradouro
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LogradouroRepository: JpaRepository<Logradouro, Int> {}