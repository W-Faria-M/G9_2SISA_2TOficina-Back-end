package com._2toficina.controller

import com._2toficina.entity.Agendamento
import com._2toficina.entity.StatusAgendamento
import com._2toficina.repository.AgendamentoRepository
import com._2toficina.repository.ExcecaoRepository
import com._2toficina.repository.FuncionamentoRepository
import com._2toficina.repository.StatusAgendamentoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.ResponseEntity
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

class AgendamentoControllerTest {

    private val agendamentoRepository = mock(AgendamentoRepository::class.java)
    private val funcionamentoRepository = mock(FuncionamentoRepository::class.java)
    private val excecaoRepository = mock(ExcecaoRepository::class.java)
    private val statusAgendamentoRepository = mock(StatusAgendamentoRepository::class.java)
    private val controller = AgendamentoController(
        agendamentoRepository,
        funcionamentoRepository,
        excecaoRepository,
        statusAgendamentoRepository
    )

    private lateinit var agendamento: Agendamento
    private val data = LocalDate.of(2024, 6, 1)
    private val hora = LocalTime.of(9, 0)

    @BeforeEach
    fun setup() {
        agendamento = Agendamento(
            id = 1,
            usuario = null,
            data = data,
            hora = hora,
            horaRetirada = null,
            descricao = "Revisão",
            observacao = null,
            statusAgendamento = StatusAgendamento(id = 1, status = "Pendente")
        )
    }

    @Test
    fun `listar agendamentos sem filtro deve retornar status 200 com lista`() {
        `when`(agendamentoRepository.findAll()).thenReturn(listOf(agendamento))

        val response = controller.listarAgendamentos(null, null, null)

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
        assertEquals(agendamento.id, response.body?.first()?.id)
    }

    @Test
    fun `listar agendamentos sem resultados deve retornar status 204`() {
        `when`(agendamentoRepository.findAll()).thenReturn(emptyList())

        val response = controller.listarAgendamentos(null, null, null)

        assertEquals(204, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `listar agendamentos filtrando por usuarioId deve retornar status 200`() {
        `when`(agendamentoRepository.findByUsuarioId(1)).thenReturn(listOf(agendamento))

        val response = controller.listarAgendamentos(1, null, null)

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
    }

    @Test
    fun `gerar horas disponiveis com funcionamento normal deve retornar lista`() {
        `when`(excecaoRepository.existsByDataInicioGreaterThanEqualAndDataFimLessThanEqual(data, data)).thenReturn(false)
        val funcionamento = mock(com._2toficina.entity.Funcionamento::class.java)
        `when`(funcionamento.inicioFuncionamento).thenReturn(LocalTime.of(8, 0))
        `when`(funcionamento.fimFuncionamento).thenReturn(LocalTime.of(12, 0))
        `when`(funcionamentoRepository.findByDiaSemana(data.dayOfWeek.value)).thenReturn(funcionamento)

        val response = controller.gerarHorasDisponiveis(data)

        assertEquals(200, response.statusCode.value())
        assertTrue(response.body!!.contains("08:00"))
    }

    @Test
    fun `gerar horas disponiveis em periodo de excecao deve lançar excecao`() {
        `when`(excecaoRepository.existsByDataInicioGreaterThanEqualAndDataFimLessThanEqual(data, data)).thenReturn(true)

        val ex = assertThrows(org.springframework.web.server.ResponseStatusException::class.java) {
            controller.gerarHorasDisponiveis(data)
        }
        assertEquals(400, ex.statusCode.value())
    }

    @Test
    fun `criar agendamento deve retornar status 201 com agendamento salvo`() {
        `when`(agendamentoRepository.save(agendamento)).thenReturn(agendamento)

        val response = controller.criarAgendamento(agendamento)

        assertEquals(201, response.statusCode.value())
        assertEquals(agendamento, response.body)
    }

    @Test
    fun `atualizar campo agendamento existente deve retornar status 200`() {
        val agendamentoAtualizado = agendamento.copy(descricao = "Nova descrição")
        val opt = Optional.of(agendamento)
        `when`(agendamentoRepository.findById(1)).thenReturn(opt)
        `when`(statusAgendamentoRepository.findById(1)).thenReturn(java.util.Optional.of(agendamento.statusAgendamento!!))
        `when`(agendamentoRepository.save(any(Agendamento::class.java))).thenReturn(agendamentoAtualizado)

        val req = Agendamento(descricao = "Nova descrição", statusAgendamento = agendamento.statusAgendamento)
        val response = controller.atualizarCampoAgendamento(1, req)

        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `atualizar campo agendamento inexistente deve retornar status 404`() {
        `when`(agendamentoRepository.findById(99)).thenReturn(Optional.empty())

        val req = Agendamento(descricao = "Qualquer")
        val response = controller.atualizarCampoAgendamento(99, req)

        assertEquals(404, response.statusCode.value())
    }
}