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
        description = """
        Retorna uma lista com os veículos cadastrados.
        Se o parâmetro 'fkUsuario' for informado, retorna apenas os veículos desse usuário.
        """)
    fun listarVeiculos(@RequestParam(required = false) fkUsuario: Int?): ResponseEntity<List<Veiculo>> {
        val veiculos = if (fkUsuario == null) {
            veiculoRepository.findAll()
        } else {
            veiculoRepository.findByFkUsuario(fkUsuario)
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
        return if (veiculoRepository.existsById(veiculoAtualizado.id)) {
            veiculoAtualizado.id = id
            val veiculoSalvo = veiculoRepository.save(veiculoAtualizado)
            ResponseEntity.status(200).body(veiculoSalvo)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/atualizar-km/{id}")
    @Operation(summary = "Atualiza a quilometragem de um veículo.",
        description = "Retorna status 200 com o veículo atualizado ou status 404 se o veículo não for encontrado.")
    fun atualizarKm(@PathVariable id: Int, @RequestParam km: Double): ResponseEntity<Veiculo> {
        val veiculoOptional = veiculoRepository.findById(id)
        return if (veiculoOptional.isPresent) {
            val veiculo = veiculoOptional.get()
            veiculo.km = km
            val veiculoSalvo = veiculoRepository.save(veiculo)
            ResponseEntity.status(200).body(veiculoSalvo)
        } else {
            ResponseEntity.status(404).build()
        }
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