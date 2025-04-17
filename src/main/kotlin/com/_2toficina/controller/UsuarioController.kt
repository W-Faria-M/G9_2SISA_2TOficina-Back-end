package com._2toficina.controller

import com._2toficina.entity.Usuario
import com._2toficina.repository.UsuarioRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/usuarios")
class UsuarioController(
    private val usuarioRepository: UsuarioRepository
) {

    @GetMapping
    fun listarUsuarios(@RequestParam(required = false) tipo: TipoUsuarioEnum?): ResponseEntity<List<Usuario>> {
        val usuarios = if (tipo == null) {
            usuarioRepository.findAll()
        } else {
            usuarioRepository.findByTipo(tipo)
        }

        return if (usuarios.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(200).body(usuarios)
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

    @DeleteMapping("/{id}")
    fun deletarUsuario(@PathVariable id: Int): ResponseEntity<Void> {
        return if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id)
            ResponseEntity.status(204).build()
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

    @PatchMapping("/alterar-email/{id}")
    fun alterarEmail(@PathVariable id: Int, @RequestParam novoEmail: String): ResponseEntity<Usuario> {
        val usuarioOptional = usuarioRepository.findById(id)
        return if (usuarioOptional.isPresent) {
            val usuario = usuarioOptional.get()
            usuario.email = novoEmail
            val usuarioSalvo = usuarioRepository.save(usuario)
            ResponseEntity.status(200).body(usuarioSalvo)
        } else {
            ResponseEntity.status(404).build()
        }
    }
}

