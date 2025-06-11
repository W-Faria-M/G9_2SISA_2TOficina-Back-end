package com._2toficina.controller

import com._2toficina.entity.Veiculo
import com._2toficina.repository.VeiculoRepository
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/veiculos")
class VeiculoController (
    private val veiculoRepository: VeiculoRepository
) {
    @GetMapping
    @Operation(summary = "Lista todos os veículos ou filtra por cliente.",
        description = """Retorna uma lista com os veículos cadastrados.
        Se o parâmetro 'fkUsuario' for informado, retorna apenas os veículos desse usuário.""")
    fun listarVeiculos(@RequestParam(required = false) usuarioId: Int?): ResponseEntity<List<Veiculo>> {
        val veiculos = if (usuarioId == null) {
            veiculoRepository.findAll()
        } else {
            veiculoRepository.findByUsuarioId(usuarioId)
        }

        return if (veiculos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(veiculos)
        }
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo veículo.",
        description = "Retorna status 201 com o veículo cadastrado ou status 400 se houver erro.")
    fun criarVeiculo(@RequestBody novoVeiculo: Veiculo): ResponseEntity<Veiculo> {
        val veiculoSalvo = veiculoRepository.save(novoVeiculo)
        return ResponseEntity.status(201).body(veiculoSalvo)
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um veículo existente.",
        description = "Retorna status 200 com o veículo atualizado ou status 404 se o veículo não for encontrado.")
    fun atualizarVeiculo(@PathVariable id: Int, @RequestBody veiculoAtualizado: Veiculo): ResponseEntity<Veiculo> {
        return if (veiculoRepository.existsById(id)) {
            veiculoAtualizado.id = id
            val veiculoSalvo = veiculoRepository.save(veiculoAtualizado)
            ResponseEntity.status(200).body(veiculoSalvo)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/atualizar-campo/{id}")
    @Operation(summary = "Atualiza um ou mais campos de um veículo específico.",
        description = "Retorna status 200 se algum campo for atualizado ou status 404 se o veículo não for encontrado.")
    fun atualizarCampoVeiculo(@PathVariable id: Int, @RequestBody req: Veiculo): ResponseEntity<Void> {
        val veiculoOpt = veiculoRepository.findById(id)
        if (veiculoOpt.isEmpty) return ResponseEntity.status(404).build()

        var veiculo = veiculoOpt.get()

        veiculo = veiculo.copy(
            placa = req.placa ?: veiculo.placa,
            marca = req.marca ?: veiculo.marca,
            modelo = req.modelo ?: veiculo.modelo,
            ano = req.ano ?: veiculo.ano,
            km = req.km ?: veiculo.km,
        )
        veiculoRepository.save(veiculo)
        return ResponseEntity.status(200).build()
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um veículo por ID.",
        description = "Retorna status 204 se o veículo for deletado ou status 404 se o veículo não for encontrado.")
    fun deletarVeiculo(@PathVariable id: Int): ResponseEntity<Void> {
        return if (veiculoRepository.existsById(id)) {
            veiculoRepository.deleteById(id)
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }
}