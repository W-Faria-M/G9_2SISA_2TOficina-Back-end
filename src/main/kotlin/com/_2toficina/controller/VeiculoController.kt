package com._2toficina.controller

import com._2toficina.entity.Veiculo
import com._2toficina.repository.VeiculoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/veiculos")
class VeiculoController (
    private val veiculoRepository: VeiculoRepository
) {
    @GetMapping
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
    fun criarVeiculo(@RequestBody novoVeiculo: Veiculo): ResponseEntity<Veiculo> {
        val veiculoSalvo = veiculoRepository.save(novoVeiculo)
        return ResponseEntity.status(201).body(veiculoSalvo)
    }

    @PutMapping("/{id}")
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
    fun deletarVeiculo(@PathVariable id: Int): ResponseEntity<Void> {
        return if (veiculoRepository.existsById(id)) {
            veiculoRepository.deleteById(id)
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }
}