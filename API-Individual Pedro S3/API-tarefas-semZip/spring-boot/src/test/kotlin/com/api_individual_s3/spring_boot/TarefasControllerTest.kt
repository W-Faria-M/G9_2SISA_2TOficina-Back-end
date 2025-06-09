package com.api_individual_s3.spring_boot
import com.api_individual_s3.spring_boot.controller.TarefasController
import com.api_individual_s3.spring_boot.entity.Tarefa
import com.api_individual_s3.spring_boot.service.TarefaService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.http.HttpStatus
import java.util.*

@ExtendWith(MockitoExtension::class)
    class TarefasControllerTest {

        @Mock
        lateinit var tarefaService: TarefaService

        @InjectMocks
        lateinit var controller: TarefasController

        @Test
        fun `criarTarefa deve retornar 201 e tarefa salva`() {
            val tarefa = Tarefa(null, "Titulo", "Desc", false)
            val tarefaSalva = tarefa.copy(id = 1)
            given(tarefaService.save(tarefa)).willReturn(tarefaSalva)

            val response = controller.criarTarefa(tarefa)

            assertEquals(HttpStatus.CREATED, response.statusCode)
            assertEquals(tarefaSalva, response.body)
        }

        @Test
        fun `listarTarefas deve retornar 200 e lista de tarefas`() {
            val tarefas = listOf(Tarefa(1, "A", "B", false))
            given(tarefaService.findAll()).willReturn(tarefas)

            val response = controller.listarTarefas("")

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(tarefas, response.body)
        }

        @Test
        fun `listarTarefas deve retornar 204 se lista vazia`() {
            given(tarefaService.findAll()).willReturn(emptyList())

            val response = controller.listarTarefas("")

            assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
            assertNull(response.body)
        }

        @Test
        fun `atualizarTarefa deve retornar 200 e tarefa atualizada`() {
            val tarefa = Tarefa(null, "Novo", "Desc", true)
            val existente = Tarefa(1, "Antigo", "Desc", false)
            given(tarefaService.findById(1)).willReturn(Optional.of(existente))
            val tarefaAtualizada = existente.copy(
                titulo = tarefa.titulo,
                descricao = tarefa.descricao,
                concluida = tarefa.concluida
            )
            given(tarefaService.save(tarefaAtualizada)).willReturn(tarefaAtualizada)

            val response = controller.atualizarTarefa(1, tarefa)

            assertEquals(HttpStatus.OK, response.statusCode)
            val body = response.body as Tarefa
            assertEquals("Novo", body.titulo)
            assertTrue(body.concluida)
        }

        @Test
        fun `atualizarTarefa deve retornar 404 se tarefa nao encontrada`() {
            val tarefa = Tarefa(null, "Novo", "Desc", true)
            given(tarefaService.findById(99)).willReturn(Optional.empty())

            val response = controller.atualizarTarefa(99, tarefa)

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
            assertEquals("Tarefa não encontrada", response.body)
        }

        @Test
        fun `deletarTarefa deve retornar 204 se existir`() {
            given(tarefaService.existsById(1)).willReturn(true)
            willDoNothing().given(tarefaService).deleteById(1)

            val response = controller.deletarTarefa(1)

            assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        }

        @Test
        fun `deletarTarefa deve retornar 404 se nao existir`() {
            given(tarefaService.existsById(99)).willReturn(false)

            val response = controller.deletarTarefa(99)

            assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        }
    }
