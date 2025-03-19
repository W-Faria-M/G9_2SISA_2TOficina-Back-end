package com._2toficina

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/usuarios")
class UsuarioController {

    val usuarios = mutableListOf<Usuario>(
        Usuario(1, "Gianluca Macedo", "gianluca.macedo@2toficina.com", "2tGian", TipoUsuarioEnum.ADMINISTRADOR),
        Usuario(2, "Thiago Ferreira", "thiago.ferreira@2toficina.com", "2tThiago", TipoUsuarioEnum.FUNCIONARIO),
        Usuario(3, "Ana Júlia", "ana.julia@gmail.com", "anaJu", TipoUsuarioEnum.CLIENTE),
        Usuario(4, "Guilherme Gomes", "guilherme.gomes@gmail.com", "GuiGomes", TipoUsuarioEnum.CLIENTE),
        Usuario(5, "Luiz Fernando", "luiz.fernando@gmail.com", "LuizFer", TipoUsuarioEnum.CLIENTE),
    )

    @GetMapping
    fun listaUsuarios(
        @RequestParam(required = false) tipo: String?
    ): ResponseEntity<List<Usuario>> {
        if (tipo == null) {
            if (usuarios.isEmpty()) {
                return ResponseEntity.status(204).build()
            }
            return ResponseEntity.status(200).body(usuarios)
        }

        val listaFiltrada = usuarios.filter {
            it.tipo.nome.contains(tipo, ignoreCase = true)
        }

        if (listaFiltrada.isEmpty()) {
            return ResponseEntity.status(204).build()
        }

        return ResponseEntity.status(200).body(listaFiltrada)
    }

    @PostMapping
    fun post(@RequestBody novoUsuario: Usuario):ResponseEntity<Usuario> {
        val novoId = if (usuarios.isEmpty()) 1 else usuarios.maxOf { it.id } + 1

        novoUsuario.id = novoId
        usuarios.add(novoUsuario)

        return ResponseEntity.status(201).body(novoUsuario)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id:Int):ResponseEntity<Void> {
        val idValido = usuarios.count { it.id == id } > 0

        if (idValido) {
            usuarios.removeIf { it.id == id }
            return ResponseEntity.status(204).build()
        }

        return ResponseEntity.status(404).build()
    }

    @PutMapping("/{id}")
    fun put(@PathVariable id:Int, @RequestBody usuarioAtualizado: Usuario):ResponseEntity<Usuario> {
        val usuarioEncontrado = usuarios.find { it.id == id }

        if (usuarioEncontrado == null) {
            return ResponseEntity.status(404).build()
        }

        val indexUsuario = usuarios.indexOf(usuarioEncontrado)
        usuarioAtualizado.id = id
        usuarios[indexUsuario] = usuarioAtualizado

        return ResponseEntity.status(200).body(usuarioAtualizado)
    }

    @PatchMapping("/alterar-email/{id}/{novoEmail}")
    fun patchEmail(@PathVariable id:Int, @PathVariable novoEmail:String): ResponseEntity<Usuario> {
        val usuarioEncontrado = usuarios.find { it.id == id }

        if (usuarioEncontrado == null) {
            return ResponseEntity.status(404).build()
        }

        usuarioEncontrado.email = novoEmail
        return ResponseEntity.status(200).body(usuarioEncontrado)
    }

}