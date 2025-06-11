package com._2toficina.controller

import com._2toficina.entity.CategoriaServico
import com._2toficina.entity.Servico
import com._2toficina.entity.ServicoAgendado
import com._2toficina.repository.CategoriaServicoRepository
import com._2toficina.repository.ServicoAgendadoRepository
import com._2toficina.repository.ServicoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class ServicoControllerTest {

    private val servicoRepository = mock(ServicoRepository::class.java)
    private val categoriaServicoRepository = mock(CategoriaServicoRepository::class.java)
    private val servicoAgendadoRepository = mock(ServicoAgendadoRepository::class.java)
    private val controller = ServicoController(servicoRepository, categoriaServicoRepository, servicoAgendadoRepository)

    private lateinit var categoria: CategoriaServico
    private lateinit var servico: Servico

    @BeforeEach
    fun setup() {
        categoria = CategoriaServico(id = 1, nome = "Revisão")
        servico = Servico(
            id = 1,
            categoriaServico = categoria,
            nome = "Troca de óleo",
            descricao = "Troca de óleo do motor",
            ehRapido = true
        )
    }

    @Test
    fun `listar serviços sem filtro deve retornar status 200 com lista`() {
        `when`(servicoRepository.findAll()).thenReturn(listOf(servico))

        val response = controller.listarServicos(null, null)

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
        assertEquals(servico.id, response.body?.first()?.id)
    }

    @Test
    fun `listar serviços filtrando por categoria deve retornar status 200 com lista`() {
        `when`(servicoRepository.findByCategoriaServicoId(1)).thenReturn(listOf(servico))

        val response = controller.listarServicos(1, null)

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
        assertEquals(servico.id, response.body?.first()?.id)
    }

    @Test
    fun `listar serviços filtrando por agendamento deve retornar status 200 com lista`() {
        val servicoAgendado = ServicoAgendado(id = 1, servico = servico)
        `when`(servicoAgendadoRepository.findByAgendamentoId(1)).thenReturn(listOf(servicoAgendado))

        val response = controller.listarServicos(null, 1)

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
        assertEquals(servico.id, response.body?.first()?.id)
    }

    @Test
    fun `listar serviços sem resultados deve retornar status 204`() {
        `when`(servicoRepository.findAll()).thenReturn(emptyList())

        val response = controller.listarServicos(null, null)

        assertEquals(204, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `criar serviço deve retornar status 201 com serviço salvo`() {
        `when`(categoriaServicoRepository.findById(1)).thenReturn(Optional.of(categoria))
        `when`(servicoRepository.save(any(Servico::class.java))).thenReturn(servico)

        val novoServico = servico.copy(id = 0)
        val response = controller.criarServico(novoServico)

        assertEquals(201, response.statusCode.value())
        assertEquals(servico, response.body)
    }

    @Test
    fun `atualizar campo serviço existente deve retornar status 200`() {
        `when`(servicoRepository.findById(servico.id)).thenReturn(Optional.of(servico))
        `when`(categoriaServicoRepository.findById(1)).thenReturn(Optional.of(categoria))
        `when`(servicoRepository.save(any(Servico::class.java))).thenReturn(servico.copy(nome = "Novo serviço"))

        val req = Servico(
            categoriaServico = categoria,
            nome = "Novo serviço",
            descricao = "Nova descrição",
            ehRapido = true
        )

        val response = controller.atualizarCampoServico(servico.id, req)

        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `atualizar campo serviço inexistente deve retornar status 404`() {
        `when`(servicoRepository.findById(99)).thenReturn(Optional.empty())

        val req = Servico(nome = "Qualquer", descricao = "Qualquer", ehRapido = false)

        val response = controller.atualizarCampoServico(99, req)

        assertEquals(404, response.statusCode.value())
    }
}