package com._2toficina.controller

import com._2toficina.dto.CepRes
import com._2toficina.entity.Endereco
import com._2toficina.repository.EnderecoRepository
import com._2toficina.repository.LogradouroRepository
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/enderecos")
class EnderecoController(
    private val enderecoRepository: EnderecoRepository,
    private val logradouroRepository: LogradouroRepository
) {

    @GetMapping
    @Operation(summary = "Lista todos os endereços",
        description = "Retorna uma lista com o cep de todos os endereços cadastrados.")
    fun listarEnderecos(): ResponseEntity<List<CepRes>> {
        return if (enderecoRepository.count() == 0L) {
            ResponseEntity.status(204).build()
        } else {
            val enderecos = enderecoRepository.findAll().map { endereco ->
                CepRes(cep = endereco.cep)
            }
            ResponseEntity.status(200).body(enderecos)
        }
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo endereço",
        description = "Retorna status 201 com o endereço cadastrado ou status 400 se houver erro.")
    fun criarEndereco(@RequestBody endereco: Endereco): ResponseEntity<Endereco> {
        val enderecoSalvo = enderecoRepository.save(endereco)
        return ResponseEntity.status(201).body(enderecoSalvo)
    }

    @PutMapping("/{usuarioId}")
    @Operation(summary = "Atualiza um endereço",
        description = "Retorna status 200 com o endereço atualizado ou status 404 se o usuário não for encontrado.")
    fun atualizarEndereco(@PathVariable usuarioId: Int,
    @RequestBody enderecoAtualizado: Endereco): ResponseEntity<Endereco> {
        return if (enderecoRepository.existsByUsuarioId(usuarioId)) {
            val enderecoExistente = enderecoRepository.findByUsuarioId(usuarioId)
            enderecoAtualizado.id = enderecoExistente.id
            enderecoAtualizado.usuario = enderecoExistente.usuario
            val enderecoSalvo = enderecoRepository.save(enderecoAtualizado)
            ResponseEntity.status(200).body(enderecoSalvo)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/atualizar-campo/{id}")
    @Operation(summary = "Atualiza um ou mais campos de um endereço específico",
        description = """ Retorna status 200 sem corpo se o campo for atualizado com sucesso,
            ou status 404 se o endereço não for encontrado, ou status 400 se algum campo não for válido.""")
    fun atualizarCampoEndereco(@PathVariable id: Int, @RequestBody req : Endereco): ResponseEntity<Void> {
        val enderecoOpt = enderecoRepository.findById(id)
        if (enderecoOpt.isEmpty) return ResponseEntity.status(404).build()

        var endereco = enderecoOpt.get()

        endereco = endereco.copy(
            tipoLogradouro =
            if (req.tipoLogradouro != null) {
                logradouroRepository.findById(req.tipoLogradouro!!.id)
                    .orElseThrow { IllegalArgumentException("Tipo de logradouro não encontrado") }
            } else {
                endereco.tipoLogradouro
            },
            nomeLogradouro = req.nomeLogradouro ?: endereco.nomeLogradouro,
            numeroLogradouro = req.numeroLogradouro ?: endereco.numeroLogradouro,
            cidade = req.cidade ?: endereco.cidade,
            estado = req.estado ?: endereco.estado,
            bairro = req.bairro ?: endereco.bairro,
            cep = req.cep ?: endereco.cep,
            complemento = req.complemento ?: endereco.complemento
        )
        enderecoRepository.save(endereco)
        return ResponseEntity.status(200).build()
    }

    @DeleteMapping("/{usuarioId}")
    @Operation(summary = "Deleta o endereço de um usuário",
        description = "Retorna status 204 se o endereço for deletado ou status 404 se o usuário não for encontrado.")
    fun deletarEndereco(@PathVariable usuarioId: Int): ResponseEntity<Void> {
        return if (enderecoRepository.existsByUsuarioId(usuarioId)) {
            enderecoRepository.deleteByUsuarioId(usuarioId)
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }
}