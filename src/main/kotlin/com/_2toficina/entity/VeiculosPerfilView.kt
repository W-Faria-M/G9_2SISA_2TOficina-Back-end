package com._2toficina.entity

import jakarta.persistence.*
import org.hibernate.annotations.Immutable

@Entity
@Immutable
@Table(name = "vw_veiculos_perfil")
data class VeiculosPerfilView(

    @Id
    @Column(name = "id_veiculo")
    val veiculoId: Int? = null,

    @Column(name = "id_usuario")
    val usuarioId: Int? = null,

    @Column(name = "marca")
    val marca: String? = null,

    @Column(name = "modelo")
    val modelo: String? = null,

    @Column(name = "ano")
    val ano: Int? = null,

    @Column(name = "km")
    val km: Int? = null,

    @Column(name = "placa")
    val placa: String? = null
) {
    constructor() : this(
        veiculoId = null,
        usuarioId = null,
        marca = null,
        modelo = null,
        ano = null,
        km = null,
        placa = null
    )
}
