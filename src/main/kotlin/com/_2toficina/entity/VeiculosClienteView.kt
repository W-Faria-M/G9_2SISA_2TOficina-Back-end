package com._2toficina.entity

import jakarta.persistence.*
import org.hibernate.annotations.Immutable

@Entity
@Immutable
@Table(name = "vw_veiculos_clientes")
data class VeiculosClienteView(
    @Id
    @Column(name = "id_veiculo")
    val veiculoId: Int?,

    @Column(name = "id_usuario")
    val usuarioId: Int?,

    @Column(name = "descricao_veiculo")
    val descricaoVeiculo: String?
) {
    // construtor vazio exigido pelo Hibernate
    constructor() : this(
        veiculoId = null,
        usuarioId = null,
        descricaoVeiculo = null
    )
}