package com._2toficina.controller

import com._2toficina.entity.Servico
import com._2toficina.entity.ServicosResumidosView
import com._2toficina.entity.VeiculosClienteView
import com._2toficina.repository.CategoriaServicoRepository
import com._2toficina.repository.ServicoAgendadoRepository
import com._2toficina.repository.ServicoRepository
import com._2toficina.repository.ServicosResumidosViewRepository
import com._2toficina.repository.VeiculosClienteViewRepository
import io.swagger.v3.oas.annotations.Operation
import org.apache.commons.lang3.mutable.Mutable
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("servicos")
class ServicoController (
    private val servicoRepository: ServicoRepository,
    private val categoriaServicoRepository: CategoriaServicoRepository,
    private val servicoAgendadoRepository: ServicoAgendadoRepository,
    private val servicosResumidosViewRepository: ServicosResumidosViewRepository
){

    @GetMapping
    @Operation(summary = "Retorna uma lista com todos os serviços cadastrados.",
        description = """Retorna status 200 com a lista de serviços (completa ou filtrada), ou
            status 204 se não houver serviços encontrados.""")
    fun listarServicos(@RequestParam(required = false) categoriaId: Int?,
                       @RequestParam(required = false) agendamentoId: Int?): ResponseEntity<List<Servico>> {
        val servicos: List<Servico> = when {
            categoriaId != null ->
                servicoRepository.findByCategoriaServicoId(categoriaId)
            agendamentoId != null ->
                servicoAgendadoRepository.findByAgendamentoId(agendamentoId).map { it.servico!! }
            else ->
                servicoRepository.findAll()
        }
        return if (servicos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(servicos)
        }
    }

    @GetMapping("/resumidos")
    @Operation(summary = "Retorna uma lista resumida com todos os serviços cadastrados.",
        description = """Retorna status 200 com a lista de serviços, ou
            status 204 se não houver serviços encontrados.""")
    fun listarServicosAgendamentos(): ResponseEntity<List<ServicosResumidosView>> {
        val servicos = servicosResumidosViewRepository.findAll()
        return if (servicos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(servicos)
        }
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo serviço.",
        description = "Retorna status 201 com o serviço cadastrado ou status 400 se houver erro.")
    fun criarServico(@RequestBody novoServico: Servico): ResponseEntity<Servico> {
        val categoriaServico = novoServico.categoriaServico?.let {
            categoriaServicoRepository.findById(it.id).orElseThrow { IllegalArgumentException("Categoria não" +
                    "encontrada") }
        }
        val servicoSalvo = servicoRepository.save(novoServico.copy(categoriaServico = categoriaServico))
        return ResponseEntity.status(201).body(servicoSalvo)
    }

    @PatchMapping("/atualizar-campo/{id}")
    @Operation(summary = "Atualiza um ou mais campos de um serviço específico.",
        description = "Retorna status 200 se algum campo for atualizado ou status 404 se o serviço não for encontrado")
    fun atualizarCampoServico(@PathVariable id: Int, @RequestBody req: Servico): ResponseEntity<Void> {
        val servicoOpt = servicoRepository.findById(id)
        if (servicoOpt.isEmpty) return ResponseEntity.status(404).build()

        var servico = servicoOpt.get()

        servico = servico.copy(
            categoriaServico =
                if (req.categoriaServico != null) {
                    categoriaServicoRepository.findById(req.categoriaServico!!.id)
                        .orElseThrow { IllegalArgumentException("Categoria não encontrada") }
                } else {
                    servico.categoriaServico
                },
            nome = req.nome ?: servico.nome,
            descricao = req.descricao ?: servico.descricao,
            ehRapido = req.ehRapido ?: servico.ehRapido
        )

        servicoRepository.save(servico)
        return ResponseEntity.status(200).build()
    }

}