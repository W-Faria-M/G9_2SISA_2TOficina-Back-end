// src/main/kotlin/com/_2toficina/repository/AvatarRepository.kt
package com._2toficina.repository

import com._2toficina.entity.Avatar
import org.springframework.data.jpa.repository.JpaRepository

interface AvatarRepository : JpaRepository<Avatar, Int> {
    fun findByUsuario_Id(usuarioId: Int): Avatar?
    fun existsByUsuario_Id(usuarioId: Int): Boolean
}