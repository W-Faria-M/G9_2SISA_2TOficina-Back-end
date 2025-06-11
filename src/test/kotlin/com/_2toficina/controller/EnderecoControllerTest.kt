package com._2toficina.controller

import com._2toficina.entity.Endereco
import com._2toficina.entity.Logradouro
import com._2toficina.entity.Usuario
import com._2toficina.repository.EnderecoRepository
import com._2toficina.repository.LogradouroRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class EnderecoControllerTest {

    private val enderecoRepository = mock(EnderecoRepository::class.java)
    private val logradouroRepository = mock(LogradouroRepository::class.java)
    private val controller = EnderecoController(enderecoRepository, logradouroRepository)

    private lateinit var endereco: Endereco
    private lateinit var usuario: Usuario
    private lateinit var logradouro: Logradouro

    @BeforeEach
    fun setup() {
        usuario = Usuario(id = 1)
        logradouro = Logradouro(id = 1, tipo = "Rua")
        endereco = Endereco(
            id = 1,
            usuario = usuario,
            tipoLogradouro = logradouro,
            nomeLogradouro = "Paulista",
            numeroLogradouro = 1000,
            cidade = "São Paulo",
            estado = "SP",
            bairro = "Centro",
            cep = "01310-100",
            complemento = "Apto 1"
        )
    }

    @Test
    fun `listar enderecos com dados deve retornar status 200 com lista`() {
        `when`(enderecoRepository.count()).thenReturn(1L)
        `when`(enderecoRepository.findAll()).thenReturn(listOf(endereco))

        val response = controller.listarEnderecos()

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
        assertEquals(endereco.cep, response.body?.first()?.cep)
    }

    @Test
    fun `listar enderecos sem dados deve retornar status 204`() {
        `when`(enderecoRepository.count()).thenReturn(0L)

        val response = controller.listarEnderecos()

        assertEquals(204, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `criar endereco deve retornar status 201 com endereco salvo`() {
        `when`(enderecoRepository.save(endereco)).thenReturn(endereco)

        val response = controller.criarEndereco(endereco)

        assertEquals(201, response.statusCode.value())
        assertEquals(endereco, response.body)
    }

    @Test
    fun `atualizar endereco existente deve retornar status 200 com endereco atualizado`() {
        val enderecoAtualizado = endereco.copy(cidade = "Campinas")
        `when`(enderecoRepository.existsByUsuarioId(usuario.id)).thenReturn(true)
        `when`(enderecoRepository.findByUsuarioId(usuario.id)).thenReturn(endereco)
        `when`(enderecoRepository.save(any(Endereco::class.java))).thenReturn(enderecoAtualizado)

        val response = controller.atualizarEndereco(usuario.id, enderecoAtualizado)

        assertEquals(200, response.statusCode.value())
        assertEquals("Campinas", response.body?.cidade)
    }

    @Test
    fun `atualizar endereco inexistente deve retornar status 404`() {
        val enderecoAtualizado = endereco.copy(cidade = "Campinas")
        `when`(enderecoRepository.existsByUsuarioId(2)).thenReturn(false)

        val response = controller.atualizarEndereco(2, enderecoAtualizado)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `atualizar campo endereco existente deve retornar status 200`() {
        val req = Endereco(cidade = "Osasco")
        `when`(enderecoRepository.findById(endereco.id)).thenReturn(Optional.of(endereco))
        `when`(enderecoRepository.save(any(Endereco::class.java))).thenReturn(endereco.copy(cidade = "Osasco"))

        val response = controller.atualizarCampoEndereco(endereco.id, req)

        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `atualizar campo endereco inexistente deve retornar status 404`() {
        val req = Endereco(cidade = "Osasco")
        `when`(enderecoRepository.findById(99)).thenReturn(Optional.empty())

        val response = controller.atualizarCampoEndereco(99, req)

        assertEquals(404, response.statusCode.value())
    }

    @Test
    fun `deletar endereco existente deve retornar status 204`() {
        `when`(enderecoRepository.existsByUsuarioId(usuario.id)).thenReturn(true)

        val response = controller.deletarEndereco(usuario.id)

        assertEquals(204, response.statusCode.value())
        verify(enderecoRepository, times(1)).deleteByUsuarioId(usuario.id)
    }

    @Test
    fun `deletar endereco inexistente deve retornar status 404`() {
        `when`(enderecoRepository.existsByUsuarioId(2)).thenReturn(false)

        val response = controller.deletarEndereco(2)

        assertEquals(404, response.statusCode.value())
        verify(enderecoRepository, never()).deleteByUsuarioId(2)
    }
}