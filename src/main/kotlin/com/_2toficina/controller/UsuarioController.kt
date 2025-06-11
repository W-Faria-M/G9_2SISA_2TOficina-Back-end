package com._2toficina.controller

import com._2toficina.dto.EnderecoRes
import com._2toficina.dto.LoginReq
import com._2toficina.dto.LoginRes
import com._2toficina.dto.UsuarioRes
import com._2toficina.entity.Usuario
import com._2toficina.repository.TipoUsuarioRepository
import com._2toficina.repository.UsuarioRepository
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/usuarios")
class UsuarioController(
    private val usuarioRepository: UsuarioRepository,
    private val tipoUsuarioRepository: TipoUsuarioRepository
) {
    val loginStatus = mutableMapOf<Int, Boolean>()

    @GetMapping
    @Operation(summary = "Lista todos os usuários ou filtra por tipo.",
        description = """Retorna uma lista com os usuários cadastrados.
        Se o parâmetro 'tipo' for informado, retorna apenas os usuários desse tipo.""")
    fun listarUsuarios(@RequestParam(required = false) tipo: Int?): ResponseEntity<List<UsuarioRes>> {
        val usuarios = if (tipo == null) {
            usuarioRepository.findAll()
        } else {
            usuarioRepository.findByTipoUsuarioId(tipo)
        }

        return if (usuarios.isEmpty()) {
            ResponseEntity.status(204).build()
        } else {
            val usuarioRes = usuarios.map { usuario ->
                UsuarioRes(
                    id = usuario.id,
                    tipoUsuario = usuario.tipoUsuario!!.tipo,
                    nomeCompleto = "${usuario.nome} ${usuario.sobrenome}",
                    telefone = usuario.telefone,
                    email = usuario.email
                )
            }
            ResponseEntity.status(200).body(usuarioRes)
        }
    }

    @PostMapping
    @Operation(summary = "Cadastra um novo usuário.",
        description = "Retorna status 201 com o usuário cadastrado ou status 400 se houver erro.")
    fun criarUsuario(@RequestBody novoUsuario: Usuario): ResponseEntity<Usuario> {
        val usuarioSalvo = usuarioRepository.save(novoUsuario)
        return ResponseEntity.status(201).body(usuarioSalvo)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um usuário por ID.",
        description = "Retorna status 200 com o usuário encontrado ou status 404 se o usuário não for encontrado.")
    fun buscarPorId(@PathVariable id: Int): ResponseEntity<Usuario> {
        val usuarioOptional = usuarioRepository.findById(id)
        return if (usuarioOptional.isPresent) {
            ResponseEntity.status(200).body(usuarioOptional.get())
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um usuário existente.",
        description = "Retorna status 200 com o usuário atualizado ou status 404 se o usuário não for encontrado.")
    fun atualizarUsuario(@PathVariable id: Int, @RequestBody usuarioAtualizado: Usuario): ResponseEntity<Usuario> {
        return if (usuarioRepository.existsById(id)) {
            usuarioAtualizado.id = id
            val usuarioSalvo = usuarioRepository.save(usuarioAtualizado)
            ResponseEntity.status(200).body(usuarioSalvo)
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PatchMapping("/atualizar-campo/{id}")
    @Operation(summary = "Atualiza um ou mais campos de um usuário específico.",
        description = """Retorna status 200 se algum campo for atualizado com sucesso, 
            status 404 se o usuário não for encontrado ou status 400 se algum campo for inválido.""")
    fun atualizarCampoUsuario(@PathVariable id: Int, @RequestBody req: Usuario): ResponseEntity<Void> {
        val usuarioOpt = usuarioRepository.findById(id)
        if (usuarioOpt.isEmpty) return ResponseEntity.status(404).build()

        var usuario = usuarioOpt.get()

        usuario = usuario.copy(
            tipoUsuario =
                if (req.tipoUsuario != null) {
                    tipoUsuarioRepository.findById(req.tipoUsuario!!.id)
                        .orElseThrow { IllegalArgumentException("Tipo de usuário não encontrado") }
                } else {
                    usuario.tipoUsuario
                },
            nome = req.nome ?: usuario.nome,
            sobrenome = req.sobrenome ?: usuario.sobrenome,
            telefone = req.telefone ?: usuario.telefone,
            email = req.email ?: usuario.email,
            dataNascimento = req.dataNascimento ?: usuario.dataNascimento
        )
        usuarioRepository.save(usuario)
        return ResponseEntity.status(200).build()
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um usuário por ID.",
        description = "Retorna status 204 se o usuário for deletado ou status 404 se o usuário não for encontrado.")
    fun deletarUsuario(@PathVariable id: Int): ResponseEntity<Void> {
        return if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id)
            ResponseEntity.status(204).build()
        } else {
            ResponseEntity.status(404).build()
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Realiza o login de um usuário.",
        description = """
        Retorna status 200 com o ID do usuário e logado como true se o login for bem-sucedido.
        Retorna status 401 com logado como false se o login falhar.
        """)
    fun login(@RequestBody loginReq: LoginReq): ResponseEntity<LoginRes> {
        val usuario = usuarioRepository.findByEmail(loginReq.email)
        return if (usuario != null && usuario.senha == loginReq.senha) {
            loginStatus[usuario.id] = true
            val resposta = LoginRes(
                usuarioId = usuario.id,
                logado = true,
            )
            ResponseEntity.status(200).body(resposta)
        } else {
            val resposta = LoginRes(
                usuarioId = null,
                logado = false,
            )
            ResponseEntity.status(401).body(resposta)
        }
    }

    @PostMapping("/logoff")
    @Operation(summary = "Realiza o logoff de um usuário.",
        description = """Retorna status 200 com o ID do usuário e logado como false se o logoff for bem-sucedido.
        Retorna status 400 com logado como false se o logoff falhar.""")
    fun logoff(@RequestParam usuarioId: Int): ResponseEntity<LoginRes> {
        if (loginStatus[usuarioId] == true) {
            loginStatus[usuarioId] = false
            val resposta = LoginRes(
                usuarioId = usuarioId,
                logado = false
            )
            return ResponseEntity.status(200).body(resposta)
        }
        return ResponseEntity.status(400).body(
            LoginRes(
                usuarioId = usuarioId,
                logado = false
            )
        )
    }
}