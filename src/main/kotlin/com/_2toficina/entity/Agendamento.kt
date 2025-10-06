package com._2toficina.entity

import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Entity
@Table(name = "agendamento")
data class Agendamento(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @JoinColumn(nullable = false, name = "fk_usuario")
    @ManyToOne
    var usuario: Usuario? = null,

    @Column(nullable = false)
    var data: LocalDate? = null,

    @Column(nullable = false)
    var hora: LocalTime? = null,

    @Column(nullable = true, name = "hora_retirada")
    var horaRetirada: String? = null,

    @Column(nullable = false)
    var veiculo: String = "",

    @Column(nullable = false, length = 256)
    var descricao: String = "",

    @JoinColumn(nullable = false, name = "fk_status_agendamento")
    @ManyToOne
    var statusAgendamento: StatusAgendamento? = null,

    @Column(nullable = true, length = 256)
    var observacao: String? = null

)