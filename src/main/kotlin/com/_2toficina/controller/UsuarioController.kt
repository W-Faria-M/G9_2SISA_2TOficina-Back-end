package com._2toficina.controller

import com._2toficina.dto.LoginRequest
import com._2toficina.dto.RespostaLogin
import com._2toficina.dto.RespostaUsuario
import com._2toficina.entity.Usuario
import com._2toficina.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/usuarios")
class UsuarioController(
    private val usuarioRepository: UsuarioRepository
) {
    private val loginStatus = mutableMapOf<Int, Boolean>()

    @GetMapping
    fun listarUsuarios(@RequestParam(required = false) tipo: Int?): ResponseEntity<List<RespostaUsuario>> {
        val usuarios = if (tipo == null) {
            usuarioRepository.findAll()
        } else {
            usuarioRepository.findByFkTipoUsuario(tipo)
        }

        return if (usuarios.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            val respostaUsuarios = usuarios.map { usuario ->
                RespostaUsuario(
                    id = usuario.id,
                    tipoUsuario = usuario.fkTipoUsuario,
                    nomeCompleto = "${usuario.nome} ${usuario.sobrenome}",
                    telefone = usuario.telefone,
                    email = usuario.email
                )
            }
            ResponseEntity.status(200).body(respostaUsuarios)
        }
    }

    @PostMapping
    fun criarUsuario(@RequestBody novoUsuario: Usuario): ResponseEntity<Usuario> {
        val usuarioSalvo = usuarioRepository.save(novoUsuario)
        return ResponseEntity.status(201).body(usuarioSalvo)
    }

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Usuario> {
        val usuarioOptional = usuarioRepository.findById(id)
        return if (usuarioOptional.isPresent) {
            ResponseEntity.status(200).body(usuarioOptional.get())
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PutMapping("/{id}")
    fun atualizarUsuario(@PathVariable id: Int, @RequestBody usuarioAtualizado: Usuario): ResponseEntity<Usuario> {
        return if (usuarioRepository.existsById(id)) {
            usuarioAtualizado.id = id
            val usuarioSalvo = usuarioRepository.save(usuarioAtualizado)
            ResponseEntity.status(200).body(usuarioSalvo)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/concluir-cadastro/{id}")
    fun concluirCadastro(@PathVariable id: Int, @RequestParam dataNasc: LocalDate, @RequestParam sexo: String): ResponseEntity<Usuario> {
        val usuarioOptional = usuarioRepository.findById(id)
        return if (usuarioOptional.isPresent) {
            val usuario = usuarioOptional.get()
            usuario.dataNascimento = dataNasc
            usuario.sexo = sexo
            val usuarioSalvo = usuarioRepository.save(usuario)
            ResponseEntity.status(200).body(usuarioSalvo)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @DeleteMapping("/{id}")
    fun deletarUsuario(@PathVariable id: Int): ResponseEntity<Void> {
        return if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id)
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<RespostaLogin> {
        val usuario = usuarioRepository.findByEmail(loginRequest.email)
        return if (usuario != null && usuario.senha == loginRequest.senha) {
            loginStatus[usuario.id] = true
            val resposta = RespostaLogin(
                usuarioId = usuario.id,
                logado = true,
            )
            ResponseEntity.status(200).body(resposta)
        } else {
            val resposta = RespostaLogin(
                usuarioId = null,
                logado = false,
            )
            ResponseEntity.status(401).body(resposta)
        }
    }

    @PostMapping("/logoff")
    fun logoff(@RequestParam usuarioId: Int): ResponseEntity<RespostaLogin> {
        if (loginStatus[usuarioId] == true) {
            loginStatus[usuarioId] = false
            val resposta = RespostaLogin(
                usuarioId = usuarioId,
                logado = false
            )
            return ResponseEntity.status(200).body(resposta)
        }
        return ResponseEntity.status(400).body(
            RespostaLogin(
                usuarioId = usuarioId,
                logado = false
            )
        )
    }
}