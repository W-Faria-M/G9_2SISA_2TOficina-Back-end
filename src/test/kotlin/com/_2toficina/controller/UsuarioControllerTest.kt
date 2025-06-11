package com._2toficina.controller

import com._2toficina.entity.Usuario
import com._2toficina.entity.TipoUsuario
import com._2toficina.repository.TipoUsuarioRepository
import com._2toficina.repository.UsuarioRepository
import com._2toficina.dto.LoginReq
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class UsuarioControllerTest {

    private val usuarioRepository = mock(UsuarioRepository::class.java)
    private val tipoUsuarioRepository = mock(TipoUsuarioRepository::class.java)
    private val controller = UsuarioController(usuarioRepository, tipoUsuarioRepository)

    private lateinit var usuario: Usuario
    private lateinit var tipoUsuario: TipoUsuario

    @BeforeEach
    fun setup() {
        tipoUsuario = TipoUsuario(id = 1, tipo = "ADMIN")
        usuario = Usuario(
            id = 1,
            tipoUsuario = tipoUsuario,
            nome = "João",
            sobrenome = "Silva",
            telefone = "11999999999",
            email = "joao@email.com",
            senha = "1234",
            dataNascimento = null
        )
    }

    @Test
    fun `listar usuários sem filtro deve retornar status 200 com lista`() {
        `when`(usuarioRepository.findAll()).thenReturn(listOf(usuario))

        val response = controller.listarUsuarios(null)

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
        assertEquals(usuario.id, response.body?.first()?.id)
    }

    @Test
    fun `listar usuários com filtro tipo deve retornar status 200 com lista`() {
        `when`(usuarioRepository.findByTipoUsuarioId(1)).thenReturn(listOf(usuario))

        val response = controller.listarUsuarios(1)

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
        assertEquals(usuario.id, response.body?.first()?.id)
    }

    @Test
    fun `listar usuários sem resultados deve retornar status 204`() {
        `when`(usuarioRepository.findAll()).thenReturn(emptyList())

        val response = controller.listarUsuarios(null)

        assertEquals(204, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `criar usuário deve retornar status 201 com usuario salvo`() {
        `when`(usuarioRepository.save(usuario)).thenReturn(usuario)

        val response = controller.criarUsuario(usuario)

        assertEquals(201, response.statusCode.value())
        assertEquals(usuario, response.body)
    }

    @Test
    fun `busca por id existente deve retornar status 200 com usuario`() {
        `when`(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario))

        val response = controller.buscarPorId(1)

        assertEquals(200, response.statusCode.value())
        assertEquals(usuario, response.body)
    }

    @Test
    fun `buscar por id inexistente deve retornar status 404`() {
        `when`(usuarioRepository.findById(2)).thenReturn(Optional.empty())

        val response = controller.buscarPorId(2)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `atualizar usuário existente deve retornar status 200 com usuario atualizado`() {
        val usuarioAtualizado = usuario.copy(nome = "Maria")
        `when`(usuarioRepository.existsById(1)).thenReturn(true)
        `when`(usuarioRepository.save(usuarioAtualizado)).thenReturn(usuarioAtualizado)

        val response = controller.atualizarUsuario(1, usuarioAtualizado)

        assertEquals(200, response.statusCode.value())
        assertEquals("Maria", response.body?.nome)
    }

    @Test
    fun `atualizar usuário inexistente deve retornar status 404`() {
        val usuarioAtualizado = usuario.copy(nome = "Maria")
        `when`(usuarioRepository.existsById(2)).thenReturn(false)

        val response = controller.atualizarUsuario(2, usuarioAtualizado)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `deletar usuário existente deve retornar status 204`() {
        `when`(usuarioRepository.existsById(1)).thenReturn(true)

        val response = controller.deletarUsuario(1)

        assertEquals(204, response.statusCode.value())
        assertNull(response.body)
        verify(usuarioRepository, times(1)).deleteById(1)
    }

    @Test
    fun `deletar usuário inexistente deve retornar status 404`() {
        `when`(usuarioRepository.existsById(2)).thenReturn(false)

        val response = controller.deletarUsuario(2)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)
        verify(usuarioRepository, never()).deleteById(2)
    }

    @Test
    fun `login com sucesso deve retornar status 200 e logado true`() {
        val loginReq = LoginReq(email = usuario.email, senha = usuario.senha)
        `when`(usuarioRepository.findByEmail(usuario.email)).thenReturn(usuario)

        val response = controller.login(loginReq)

        assertEquals(200, response.statusCode.value())
        assertTrue(response.body?.logado == true)
        assertEquals(usuario.id, response.body?.usuarioId)
    }

    @Test
    fun `login com falha deve retornar status 401 e logado false`() {
        val loginReq = LoginReq(email = usuario.email, senha = "errada")
        `when`(usuarioRepository.findByEmail(usuario.email)).thenReturn(usuario)

        val response = controller.login(loginReq)

        assertEquals(401, response.statusCode.value())
        assertFalse(response.body?.logado == true)
        assertNull(response.body?.usuarioId)
    }

    @Test
    fun `logoff com usuario logado deve retornar status 200 e logado false`() {
        controller.loginStatus[usuario.id] = true

        val response = controller.logoff(usuario.id)

        assertEquals(200, response.statusCode.value())
        assertFalse(response.body?.logado == true)
        assertEquals(usuario.id, response.body?.usuarioId)
    }

    @Test
    fun `logoff com usuario nao logado deve retornar status 400`() {
        controller.loginStatus[usuario.id] = false

        val response = controller.logoff(usuario.id)

        assertEquals(400, response.statusCode.value())
        assertFalse(response.body?.logado == true)
        assertEquals(usuario.id, response.body?.usuarioId)
    }
}