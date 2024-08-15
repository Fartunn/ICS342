package com.example.assignment2

import android.app.Application
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutinesTestExtension::class, InstantExecutorExtension::class)
class TodoListViewModelTest {

    private val mockApiService = mockk<TodoApiService>()
    private val mockApplication = mockk<Application>(relaxed = true)
    private val viewModel = TodoListViewModel(mockApplication)

    @Test
    fun `should fetchTodos successfully when API call is successful`() = runTest {
        val mockTodos = listOf(mockk<TodoItem>(relaxed = true))
        coEvery { mockApiService.getTodos(any(), any(), any()) } returns mockTodos

        viewModel.fetchTodos()

        coVerify(exactly = 1) { mockApiService.getTodos(any(), any(), any()) }
        assertEquals(mockTodos, viewModel.todos.value)
    }

    @Test
    fun `should return error when fetching todos fails`() = runTest {
        coEvery { mockApiService.getTodos(any(), any(), any()) } throws Exception("Network error")

        viewModel.fetchTodos()

        coVerify(exactly = 1) { mockApiService.getTodos(any(), any(), any()) }
        assertEquals("Failed to fetch todos", viewModel.error.value)
    }

    @Test
    fun `should createTodo successfully when API call is successful`() = runTest {
        val newTodo = mockk<TodoItem>(relaxed = true)
        coEvery { mockApiService.createTodo(any(), any(), any(), any()) } returns newTodo

        viewModel.createTodo("New Todo Item")

        coVerify(exactly = 1) { mockApiService.createTodo(any(), any(), any(), any()) }
        coVerify(exactly = 1) { mockApiService.getTodos(any(), any(), any()) }
    }

    @Test
    fun `should return error when creating todo fails`() = runTest {
        coEvery { mockApiService.createTodo(any(), any(), any(), any()) } throws Exception("Network error")

        viewModel.createTodo("New Todo Item")

        coVerify(exactly = 1) { mockApiService.createTodo(any(), any(), any(), any()) }
        assertEquals("Failed to create todo: Network error", viewModel.error.value)
    }
}
