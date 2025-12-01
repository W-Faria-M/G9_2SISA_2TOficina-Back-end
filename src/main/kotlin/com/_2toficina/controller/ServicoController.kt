// Kotlin
package com._2toficina.controller

import com._2toficina.dto.QuantidadeServicosPorMesRes
import com._2toficina.entity.CategoriaServico
import com._2toficina.entity.QuantidadeServicosPorMesView
import com._2toficina.entity.Servico
import com._2toficina.entity.ServicosCompletosView
import com._2toficina.entity.ServicosResumidosView
import com._2toficina.repository.CategoriaServicoRepository
import com._2toficina.repository.QuantidadeServicosPorMesRepository
import com._2toficina.repository.ServicoAgendadoRepository
import com._2toficina.repository.ServicoRepository
import com._2toficina.repository.ServicosCompletosViewRepository
import com._2toficina.repository.ServicosResumidosViewRepository
import com._2toficina.repository.StatusServicoRepository
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("servicos")
class ServicoController(
    private val servicoRepository: ServicoRepository,
    private val categoriaServicoRepository: CategoriaServicoRepository,
    private val servicoAgendadoRepository: ServicoAgendadoRepository,
    private val servicosResumidosViewRepository: ServicosResumidosViewRepository,
    private val statusServicoRepository: StatusServicoRepository,
    private val servicosCompletosViewRepository: ServicosCompletosViewRepository,
    private val quantidadeServicosPorMesRepository: QuantidadeServicosPorMesRepository
) {

    @GetMapping
    @Operation(summary = "Retorna todos os serviços cadastrados.")
    fun listarServicos(
        @RequestParam(required = false) categoriaId: Int?,
        @RequestParam(required = false) agendamentoId: Int?
    ): ResponseEntity<List<Servico>> {
        val servicos: List<Servico> = when {
            categoriaId != null ->
                servicoRepository.findByCategoriaServicoId(categoriaId)
            agendamentoId != null ->
                servicoAgendadoRepository.findByAgendamentoId(agendamentoId).mapNotNull { it.servico }
            else ->
                servicoRepository.findAll()
        }
        return ResponseEntity.ok(servicos)
    }

    @GetMapping("/resumidos")
    @Operation(summary = "Lista resumida de serviços para seleção no agendamento.")
    fun listarServicosAgendamentos(): ResponseEntity<List<ServicosResumidosView>> {
        val servicos = servicosResumidosViewRepository.findByStatus(1)
        return ResponseEntity.ok(servicos)
    }

    @GetMapping("/completos")
    @Operation(summary = "Lista completa para gerenciamento de serviços.")
    fun listarServicosOficina(): ResponseEntity<List<ServicosCompletosView>> {
        val servicos = servicosCompletosViewRepository.findAll()
        return ResponseEntity.ok(servicos)
    }

    @GetMapping("/categorias")
    @Operation(summary = "Lista categorias de serviço.")
    fun listarCategorias(): ResponseEntity<List<CategoriaServico>> {
        val categorias = categoriaServicoRepository.findAll()
        return ResponseEntity.ok(categorias)
    }

    @PostMapping("/categorias")
    @Operation(summary = "Cria uma nova categoria de serviço.")
    fun criarCategoria(@RequestBody novaCategoria: CategoriaServico): ResponseEntity<CategoriaServico> {
        val categoriaSalva = categoriaServicoRepository.save(novaCategoria)
        return ResponseEntity.status(201).body(categoriaSalva)
    }

    @PutMapping("/categorias/{id}")
    @Operation(summary = "Atualiza o nome de uma categoria.")
    fun atualizarCategoria(
        @PathVariable id: Int,
        @RequestBody req: CategoriaServico
    ): ResponseEntity<CategoriaServico> {
        val categoriaOpt = categoriaServicoRepository.findById(id)
        if (categoriaOpt.isEmpty) return ResponseEntity.status(404).build()
        val categoriaAtualizada = categoriaServicoRepository.save(
            categoriaOpt.get().copy(nome = req.nome)
        )
        return ResponseEntity.ok(categoriaAtualizada)
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo serviço.")
    fun criarServico(@RequestBody novoServico: Servico): ResponseEntity<Servico> {
        val categoriaServico = novoServico.categoriaServico?.let {
            categoriaServicoRepository.findById(it.id)
                .orElseThrow { IllegalArgumentException("Categoria não encontrada") }
        }
        val servicoSalvo = servicoRepository.save(novoServico.copy(categoriaServico = categoriaServico))
        return ResponseEntity.status(201).body(servicoSalvo)
    }

    @PatchMapping("/atualizar-campo/{id}")
    @Operation(summary = "Atualiza um ou mais campos de um serviço.")
    fun atualizarCampoServico(@PathVariable id: Int, @RequestBody req: Servico): ResponseEntity<Void> {
        val servicoOpt = servicoRepository.findById(id)
        if (servicoOpt.isEmpty) return ResponseEntity.status(404).build()

        var servico = servicoOpt.get()
        servico = servico.copy(
            categoriaServico =
                if (req.categoriaServico != null)
                    categoriaServicoRepository.findById(req.categoriaServico!!.id)
                        .orElseThrow { IllegalArgumentException("Categoria não encontrada") }
                else servico.categoriaServico,
            nome = req.nome ?: servico.nome,
            descricao = req.descricao ?: servico.descricao,
            ehRapido = req.ehRapido ?: servico.ehRapido,
            statusServico =
                if (req.statusServico != null)
                    statusServicoRepository.findById(req.statusServico!!.id)
                        .orElseThrow { IllegalArgumentException("Status não encontrado") }
                else servico.statusServico
        )

        servicoRepository.save(servico)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/estatisticas/quantidade-por-mes")
    @Operation(summary = "Quantidade de serviços executados por mês.")
    fun quantidadePorMes(
        @RequestParam(required = false) ano: Int?,
        @RequestParam(required = false) servicoId: Int?
    ): ResponseEntity<List<QuantidadeServicosPorMesRes>> {
        val registros: List<QuantidadeServicosPorMesView> = when {
            ano != null && servicoId != null ->
                quantidadeServicosPorMesRepository.findByIdAnoAndIdIdServico(ano, servicoId)
            ano != null ->
                quantidadeServicosPorMesRepository.findByIdAno(ano)
            else ->
                quantidadeServicosPorMesRepository.findAll()
        }

        val resposta = registros.map {
            QuantidadeServicosPorMesRes(
                ano = it.id.ano,
                mes = it.id.mes,
                idServico = it.id.idServico,
                nomeServico = it.nomeServico,
                totalServicos = it.totalServicos
            )
        }.sortedWith(compareBy({ it.ano }, { it.mes }, { it.nomeServico }))

        return ResponseEntity.ok(resposta)
    }
}