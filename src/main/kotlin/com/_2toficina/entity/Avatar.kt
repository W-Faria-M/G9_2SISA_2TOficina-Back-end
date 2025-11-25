// src/main/kotlin/com/_2toficina/entity/Avatar.kt
package com._2toficina.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.*

@Entity
@Table(name = "avatar")
data class Avatar(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "fk_usuario", nullable = false, unique = true)
    var usuario: Usuario? = null,

    @Column(nullable = false)
    var avatarStyle: String = "",
    @Column(nullable = false)
    var topType: String = "",
    @Column(nullable = false)
    var accessoriesType: String = "",
    @Column(nullable = false)
    var facialHairType: String = "",
    @Column(nullable = false)
    var clotheType: String = "",
    @Column(nullable = false)
    var eyeType: String = "",
    @Column(nullable = false)
    var eyebrowType: String = "",
    @Column(nullable = false)
    var mouthType: String = "",
    @Column(nullable = false)
    var skinColor: String = "",


) {
    @JsonProperty("usuarioId")
    fun getUsuarioId(): Int? = usuario?.id
}