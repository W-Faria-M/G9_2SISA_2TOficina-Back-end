package com._2toficina.controller

import com._2toficina.entity.Agendamento
import com._2toficina.repository.AgendamentoRepository
import com._2toficina.repository.ExcecaoRepository
import com._2toficina.repository.FuncionamentoRepository
import com._2toficina.repository.StatusAgendamentoRepository
import io.swagger.v3.oas.annotations.Operation
import org.apache.el.stream.Optional
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicReference
import kotlin.toString

@Controller
@RequestMapping("/agendamentos")
class AgendamentoController(
    private val agendamentoRepository: AgendamentoRepository,
    private val funcionamentoRepository: FuncionamentoRepository,
    private val excecaoRepository: ExcecaoRepository,
    private val statusAgendamentoRepository: StatusAgendamentoRepository
) {

    @GetMapping
    @Operation(summary = "Lista todos os agendamentos cadastrados.",
        description = "Retorna status 200 com a lista de agendamentos ou status 204 se não houver agendamentos.")
    fun listarAgendamentos(@RequestParam(required = false) usuarioId: Int?,
                           @RequestParam(required = false) data: LocalDate?,
                           @RequestParam(required = false) statusId: Int?): ResponseEntity<List<Agendamento>> {
        val agendamentos: List<Agendamento> = when {
            usuarioId != null && data != null && statusId != null ->
                agendamentoRepository.findByUsuarioId(usuarioId)
                    .filter { it.data == data && it.statusAgendamento?.id == statusId }
            usuarioId != null && data != null ->
                agendamentoRepository.findByUsuarioId(usuarioId)
                    .filter { it.data == data }
            usuarioId != null && statusId != null ->
                agendamentoRepository.findByUsuarioId(usuarioId)
                    .filter { it.statusAgendamento?.id == statusId }
            data != null && statusId != null ->
                agendamentoRepository.findByStatusAgendamentoId(statusId)
                    .filter { it.data == data }
            usuarioId != null ->
                agendamentoRepository.findByUsuarioId(usuarioId)
            data != null ->
                agendamentoRepository.findByData(data)
            statusId != null ->
                agendamentoRepository.findByStatusAgendamentoId(statusId)
            else ->
                agendamentoRepository.findAll()
        }
        return if (agendamentos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            return ResponseEntity.status(200).body(agendamentos)
        }
    }

    @GetMapping("/gerar-horas-disponiveis")
    @Operation(summary = "Gera uma lista de horas disponíveis para agendamento.",
        description = "todo")
    fun gerarHorasDisponiveis(@RequestParam data: LocalDate): ResponseEntity<List<String>> {
        val haExcecoes = excecaoRepository.existsByDataInicioGreaterThanEqualAndDataFimLessThanEqual(data, data)
        if (haExcecoes) {
            throw ResponseStatusException(400, "A data informada está em um período de exceção.", null)
        }

        val funcionamento = funcionamentoRepository.findByDiaSemana(data.dayOfWeek.value)

        val horariosDisponiveis = mutableListOf<String>()
        var hora = funcionamento.inicioFuncionamento
        while (hora!!.isBefore(funcionamento.fimFuncionamento)) {
            horariosDisponiveis.add(hora.toString())
            hora = hora.plusHours(1)
        }

        return ResponseEntity.ok(horariosDisponiveis)
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo agendamento.",
        description = "Retorna status 201 com o agendamento cadastrado ou status 400 se houver erro.")
    fun criarAgendamento(@RequestBody novoAgendamento: Agendamento): ResponseEntity<Agendamento> {
        val agendamentoSalvo = agendamentoRepository.save(novoAgendamento)
        return ResponseEntity.status(201).body(agendamentoSalvo)
    }

    @PatchMapping("/atualizar-campo/{id}")
    @Operation(summary = "Atualiza um ou mais campos de um agendamento específico.",
        description = """Retorna status 200 se algum campo for atualizado com sucesso,
            status 404 se o agendamento não for encontrado ou status 400 se algum campo for inválido.""")
    fun atualizarCampoAgendamento(@PathVariable id: Int, @RequestBody req: Agendamento
    ): ResponseEntity<Void> {
        val agendamentoOpt = agendamentoRepository.findById(id)
        if (agendamentoOpt.isEmpty) return ResponseEntity.status(404).build()

        var agendamento = agendamentoOpt.get()

        agendamento = agendamento.copy(
            data = req.data ?: agendamento.data,
            hora = req.hora ?: agendamento.hora,
            horaRetirada = req.horaRetirada ?: agendamento.horaRetirada,
            descricao = req.descricao ?: agendamento.descricao,
            observacao = req.observacao ?: agendamento.observacao,
            statusAgendamento =
            if (req.statusAgendamento != null) {
                statusAgendamentoRepository.findById(req.statusAgendamento!!.id)
                    .orElseThrow { IllegalArgumentException("Status de agendamento não encontrado") }
            } else {
                agendamento.statusAgendamento
            }
        )
        agendamentoRepository.save(agendamento)
        return ResponseEntity.status(200).build()
    }

}