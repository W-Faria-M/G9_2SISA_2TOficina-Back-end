package com._2toficina.controller

import com._2toficina.dto.AgendamentoRes
import com._2toficina.entity.Agendamento
import com._2toficina.entity.AgendamentoClienteView
import com._2toficina.repository.AgendamentoClienteViewRepository
import com._2toficina.repository.AgendamentoRepository
import com._2toficina.repository.ExcecaoRepository
import com._2toficina.repository.FuncionamentoRepository
import com._2toficina.repository.StatusAgendamentoRepository
import io.swagger.v3.oas.annotations.Operation
import org.apache.el.stream.Optional
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import com._2toficina.repository.ServicoAgendadoRepository
import java.util.concurrent.atomic.AtomicReference
import kotlin.toString

@Controller
@RequestMapping("/agendamentos")
class AgendamentoController(
    private val agendamentoRepository: AgendamentoRepository,
    private val funcionamentoRepository: FuncionamentoRepository,
    private val excecaoRepository: ExcecaoRepository,
    private val statusAgendamentoRepository: StatusAgendamentoRepository,
    private val agendamentoClienteViewRepository: AgendamentoClienteViewRepository,
    private val servicoAgendadoRepository: ServicoAgendadoRepository
) {

    @GetMapping
    @Operation(summary = "Lista todos os agendamentos cadastrados.",
        description = "Retorna status 200 com a lista de agendamentos ou status 204 se não houver agendamentos.")
    fun listarAgendamentos(
        @RequestParam(required = false) usuarioId: Int?
    ): ResponseEntity<List<AgendamentoClienteView>> {

        val agendamentos = if (usuarioId != null)
            agendamentoClienteViewRepository.findByUsuarioId(usuarioId)
        else
            agendamentoClienteViewRepository.findAll()

        return if (agendamentos.isEmpty())
            ResponseEntity.noContent().build()
        else
            ResponseEntity.ok(agendamentos)
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
        if (funcionamento == null) {
            throw ResponseStatusException(400, "Dia da semana informado fora do funcionamento.", null)
        }
        val agendamentos = agendamentoRepository.findByData(data)
        val horariosAgendados = agendamentos.mapNotNull { it.hora }.toSet()

        val horariosDisponiveis = mutableListOf<String>()
        var hora = funcionamento.inicioFuncionamento
        while (hora!!.isBefore(funcionamento.fimFuncionamento)) {
            if (!horariosAgendados.contains(hora)) {
                horariosDisponiveis.add(hora.toString())
            }
            hora = hora.plusHours(2)
        }
        return ResponseEntity.ok(horariosDisponiveis)
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo agendamento.",
        description = "Retorna status 201 com o agendamento cadastrado ou status 400 se houver erro.")
    fun criarAgendamento(@RequestBody novoAgendamento: Agendamento): ResponseEntity<AgendamentoRes> {
        if (novoAgendamento.data == null || novoAgendamento.hora == null) {
            return ResponseEntity.status(400).build()
        }

        val funcionamento = funcionamentoRepository.findByDiaSemana(novoAgendamento.data!!.dayOfWeek.value)
        if (funcionamento == null) {
            throw ResponseStatusException(400, "Dia da semana informado fora do funcionamento.", null)
        }

        val horaAgendamento = novoAgendamento.hora!!
        if (horaAgendamento.isBefore(funcionamento.inicioFuncionamento) || !horaAgendamento.isBefore(funcionamento.fimFuncionamento)) {
            throw ResponseStatusException(400, "Horário fora do funcionamento.", null)
        }

        val existe = agendamentoRepository.existsByDataAndHora(novoAgendamento.data!!, novoAgendamento.hora!!)
        if (existe) {
            throw ResponseStatusException(400, "Horário já reservado.", null)
        }
        val agendamentoSalvo = agendamentoRepository.save(novoAgendamento)

        val resposta = AgendamentoRes(
            nome = agendamentoSalvo.usuario?.nome ?: "",
            sobrenome = agendamentoSalvo.usuario?.sobrenome ?: "",
            data = agendamentoSalvo.data!!,
            hora = agendamentoSalvo.hora!!,
            horaRetirada = agendamentoSalvo.horaRetirada,
            veiculo = agendamentoSalvo.veiculo,
            descricao = agendamentoSalvo.descricao,
            statusAgendamento = agendamentoSalvo.statusAgendamento?.status ?: "",
            observacao = agendamentoSalvo.observacao
        )
        return ResponseEntity.status(201).body(resposta)
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um agendamento por ID.",
        description = "Retorna status 204 se o agendamento for deletado ou status 404 se o agendamento não for encontrado.")
    fun deletarAgendamento(@PathVariable id: Int): ResponseEntity<Void> {
        println("Tentando excluir agendamento com ID: $id")
        return try {
            val agendamentoOpt = agendamentoRepository.findById(id)
            if (agendamentoOpt.isEmpty) {
                println("Agendamento não encontrado: $id")
                return ResponseEntity.status(404).build()
            }

            val agendamento = agendamentoOpt.get()

            servicoAgendadoRepository.deleteByAgendamentoId(id)

            agendamentoRepository.delete(agendamento)
            println("Agendamento excluído com sucesso: $id")
            ResponseEntity.noContent().build()
        } catch (ex: org.springframework.dao.DataIntegrityViolationException) {
            ex.printStackTrace()
            println("Violação de integridade ao excluir agendamento $id: ${ex.message}")
            ResponseEntity.status(409).build()
        } catch (ex: Exception) {
            ex.printStackTrace()
            println("Erro inesperado ao excluir agendamento $id: ${ex.message}")
            ResponseEntity.status(500).build()
        }
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
                    .orElseThrow { IllegalArgumentException("Status de agendamento não encontrado.") }
            } else {
                agendamento.statusAgendamento
            }
        )
        agendamentoRepository.save(agendamento)
        return ResponseEntity.status(200).build()
    }

}