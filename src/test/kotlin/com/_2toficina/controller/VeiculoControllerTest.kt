package com._2toficina.controller

import com._2toficina.entity.Veiculo
import com._2toficina.repository.VeiculoRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*

class VeiculoControllerTest {

    private val veiculoRepository = mock(VeiculoRepository::class.java)
    private val controller = VeiculoController(veiculoRepository)

    private lateinit var veiculo: Veiculo

    @BeforeEach
    fun setup() {
        veiculo = Veiculo(
            id = 1,
            usuario = null,
            placa = "ABC1234",
            marca = "Honda",
            modelo = "CG 160",
            ano = 2020,
            km = 15000.0
        )
    }

    @Test
    fun `listar veículos sem filtro deve retornar status 200 com lista`() {
        `when`(veiculoRepository.findAll()).thenReturn(listOf(veiculo))

        val response = controller.listarVeiculos(null)

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
        assertEquals(veiculo.id, response.body?.first()?.id)
    }

    @Test
    fun `listar veículos com filtro deve retornar status 200 com lista`() {
        `when`(veiculoRepository.findByUsuarioId(1)).thenReturn(listOf(veiculo))

        val response = controller.listarVeiculos(1)

        assertEquals(200, response.statusCode.value())
        assertEquals(1, response.body?.size)
        assertEquals(veiculo.id, response.body?.first()?.id)
    }

    @Test
    fun `listar veículos sem resultados deve retornar status 204`() {
        `when`(veiculoRepository.findAll()).thenReturn(emptyList())

        val response = controller.listarVeiculos(null)

        assertEquals(204, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `criar veículo deve retornar status 201 com veículo salvo`() {
        `when`(veiculoRepository.save(veiculo)).thenReturn(veiculo)

        val response = controller.criarVeiculo(veiculo)

        assertEquals(201, response.statusCode.value())
        assertEquals(veiculo, response.body)
    }

    @Test
    fun `atualizar veículo existente deve retornar status 200 com veículo atualizado`() {
        val veiculoAtualizado = veiculo.copy(marca = "Yamaha")
        `when`(veiculoRepository.existsById(1)).thenReturn(true)
        `when`(veiculoRepository.save(veiculoAtualizado)).thenReturn(veiculoAtualizado)

        val response = controller.atualizarVeiculo(1, veiculoAtualizado)

        assertEquals(200, response.statusCode.value())
        assertEquals("Yamaha", response.body?.marca)
    }

    @Test
    fun `atualizar veículo inexistente deve retornar status 404`() {
        val veiculoAtualizado = veiculo.copy(marca = "Yamaha")
        `when`(veiculoRepository.existsById(2)).thenReturn(false)

        val response = controller.atualizarVeiculo(2, veiculoAtualizado)

        assertEquals(404, response.statusCode.value())
        assertNull(response.body)
    }

    @Test
    fun `atualizar campo veículo existente deve retornar status 200`() {
        val req = Veiculo(marca = "Suzuki")
        `when`(veiculoRepository.findById(veiculo.id)).thenReturn(Optional.of(veiculo))
        `when`(veiculoRepository.save(any(Veiculo::class.java))).thenReturn(veiculo.copy(marca = "Suzuki"))

        val response = controller.atualizarCampoVeiculo(veiculo.id, req)

        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `atualizar campo veículo inexistente deve retornar status 404`() {
        val req = Veiculo(marca = "Suzuki")
        `when`(veiculoRepository.findById(99)).thenReturn(Optional.empty())

        val response = controller.atualizarCampoVeiculo(99, req)

        assertEquals(404, response.statusCode.value())
    }

    @Test
    fun `deletar veículo existente deve retornar status 204`() {
        `when`(veiculoRepository.existsById(1)).thenReturn(true)

        val response = controller.deletarVeiculo(1)

        assertEquals(204, response.statusCode.value())
        verify(veiculoRepository, times(1)).deleteById(1)
    }

    @Test
    fun `deletar veículo inexistente deve retornar status 404`() {
        `when`(veiculoRepository.existsById(2)).thenReturn(false)

        val response = controller.deletarVeiculo(2)

        assertEquals(404, response.statusCode.value())
        verify(veiculoRepository, never()).deleteById(2)
    }
}