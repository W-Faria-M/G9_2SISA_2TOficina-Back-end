package com.api_individual_s3.spring_boot.controller

import com.api_individual_s3.spring_boot.entity.Tarefa
import com.api_individual_s3.spring_boot.service.TarefaService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/tarefas")
class TarefasController(private val tarefaService: TarefaService) {

    @Operation(summary = "Cria uma nova tarefa", description = "Retorna a tarefa criada com status 201")
    @PostMapping
    fun criarTarefa(@RequestBody tarefa: Tarefa): ResponseEntity<Tarefa> {
        val tarefaSalva = tarefaService.save(tarefa)
        return ResponseEntity.status(201).body(tarefaSalva)
    }

    @Operation(summary = "Lista todas as tarefas", description = "Retorna a tarefa encontrada ou status 204 se não existir")
    @GetMapping
    fun listarTarefas(@RequestParam(required = false, defaultValue = "") titulo: String): ResponseEntity<List<Tarefa>> {
        val tarefas = if (titulo.isNotEmpty()) {
            tarefaService.findByTituloIgnoreCaseContaining(titulo)
        } else {
            tarefaService.findAll()
        }
        return if (tarefas.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(tarefas)
        }
    }

    @Operation(summary = "Atualiza uma tarefa", description = "Atualiza os dados de uma tarefa existente")
    @PutMapping("/{id}")
    fun atualizarTarefa(@PathVariable id: Int, @RequestBody tarefa: Tarefa): ResponseEntity<Any> {
        val existente = tarefaService.findById(id)
        if (existente.isEmpty) return ResponseEntity.status(404).body("Tarefa não encontrada")

        val atualizada = existente.get().copy(
            titulo = tarefa.titulo,
            descricao = tarefa.descricao,
            concluida = tarefa.concluida
        )

        tarefaService.save(atualizada)
        return ResponseEntity.ok(atualizada)
    }

    @Operation(summary = "Deleta uma tarefa", description = "Remove a tarefa com base no ID")
    @DeleteMapping("/{id}")
    fun deletarTarefa(@PathVariable id: Int): ResponseEntity<Void> {
        return if (tarefaService.existsById(id)) {
            tarefaService.deleteById(id)
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }
}