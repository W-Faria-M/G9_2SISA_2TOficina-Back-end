package com._2toficina.entity

import jakarta.persistence.*
import org.hibernate.annotations.Immutable
import java.time.LocalDate
import java.time.LocalTime

@Entity
@Immutable
@Table(name = "vw_agendamentos_clientes")
data class AgendamentoClienteView(
    @Id
    @Column(name = "id_agendamento")
    val agendamentoId: Int?,

    @Column(name = "id_usuario")
    val usuarioId: Int?,

    @Column(name = "nome_veiculo")
    val nomeVeiculo: String?,

    @Column(name = "data_agendamento")
    val dataAgendamento: LocalDate?,

    @Column(name = "hora_agendamento")
    val horaAgendamento: LocalTime?,

    @Column(name = "hora_retirada")
    val horaRetirada: LocalTime?,

    @Column(name = "status")
    val status: String?,

    @Column(name = "descricao")
    val descricao: String?,

    @Column(name = "observacao")
    val observacao: String?,

    @Column(name = "servicos")
    val servicos: String?
) {
    // construtor vazio exigido pelo Hibernate
    constructor() : this(
        agendamentoId = null,
        dataAgendamento = null,
        descricao = null,
        horaAgendamento = null,
        horaRetirada = null,
        usuarioId = null,
        nomeVeiculo = null,
        observacao = null,
        servicos = null,
        status = null
    )
}