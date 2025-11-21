package com._2toficina.controller

import com._2toficina.entity.Veiculo
import com._2toficina.entity.VeiculosClienteView
import com._2toficina.entity.VeiculosPerfilView
import com._2toficina.repository.VeiculoRepository
import com._2toficina.repository.VeiculosClienteViewRepository
import com._2toficina.repository.VeiculosPerfilViewRepository
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/veiculos")
class VeiculoController (
    private val veiculoRepository: VeiculoRepository,
    private val veiculosClienteViewRepository: VeiculosClienteViewRepository,
    private val veiculosPerfilViewRepository: VeiculosPerfilViewRepository
) {
    @GetMapping
    @Operation(
        summary = "Lista veículos de um cliente.",
        description = """
        Retorna os veículos pertencentes ao usuário informado via parâmetro 'usuarioId'.
        Os dados são obtidos diretamente da view 'vw_veiculos_usuario', já formatados com marca, modelo e placa.
    """
    )
    fun listarVeiculos(@RequestParam usuarioId: Int): ResponseEntity<List<VeiculosClienteView>> {
        val veiculos = veiculosClienteViewRepository.findByUsuarioId(usuarioId)

        return if (veiculos.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(veiculos)
        }
    }

    @GetMapping("/perfil")
    @Operation(
        summary = "Lista os veículos exibidos no perfil do usuário.",
        description = """
        Retorna os veículos do usuário informados via parâmetro 'usuarioId'.
        Os dados são obtidos da view 'vw_veiculos_perfil'.
    """
    )
    fun listarVeiculosPerfil(@RequestParam usuarioId: Int): ResponseEntity<List<VeiculosPerfilView>> {
        val veiculos = veiculosPerfilViewRepository.findByUsuarioId(usuarioId)

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

//    @PatchMapping("/atualizar-campo/{id}")
//    @Operation(summary = "Atualiza um ou mais campos de um veículo específico.",
//        description = "Retorna status 200 se algum campo for atualizado ou status 404 se o veículo não for encontrado.")
//    fun atualizarCampoVeiculo(@PathVariable id: Int, @RequestBody req: Veiculo): ResponseEntity<Void> {
//        val veiculoOpt = veiculoRepository.findById(id)
//        if (veiculoOpt.isEmpty) return ResponseEntity.status(404).build()
//
//        var veiculo = veiculoOpt.get()
//
//        veiculo = veiculo.copy(
//            placa = req.placa ?: veiculo.placa,
//            marca = req.marca ?: veiculo.marca,
//            modelo = req.modelo ?: veiculo.modelo,
//            ano = req.ano ?: veiculo.ano,
//            km = req.km ?: veiculo.km,
//        )
//        veiculoRepository.save(veiculo)
//        return ResponseEntity.status(200).build()
//    }

    @PatchMapping("/atualizar-campo/{id}")
    fun atualizarCampoVeiculo(@PathVariable id: Int, @RequestBody req: Veiculo): ResponseEntity<Void> {
        val veiculo = veiculoRepository.findById(id).orElse(null)
            ?: return ResponseEntity.status(404).build()

        req.km?.let { veiculo.km = it }
        req.placa?.takeIf { it.isNotBlank() }?.let { veiculo.placa = it }
        req.marca?.takeIf { it.isNotBlank() }?.let { veiculo.marca = it }
        req.modelo?.takeIf { it.isNotBlank() }?.let { veiculo.modelo = it }
        req.ano?.let { veiculo.ano = it }

        veiculoRepository.save(veiculo)

        return ResponseEntity.ok().build()
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