package com.example.assignment2

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TodoListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TodoListViewModel
    private val mockApiService = mockk<TodoApiService>()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Set the main dispatcher for testing
        val mockApplication = mockk<Application>(relaxed = true) // Create a relaxed mock of the Application
        viewModel = TodoListViewModel(mockApplication)
        mockkObject(ApiService)
        every { ApiService.apiService } returns mockApiService
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Reset the main dispatcher
        unmockkAll()
    }

    @Test
    fun `fetchTodos should succeed when valid user and token are provided`() = runTest {
        val todosList = listOf(TodoItem(1, "Test Todo", false))
        coEvery { mockApiService.getTodos(any(), any(), any()) } returns todosList

        viewModel.fetchTodos()
        assert(viewModel.todos.value == todosList)

        coVerify { mockApiService.getTodos(any(), any(), any()) }
    }

    @Test
    fun `fetchTodos should fail when an error occurs`() = runTest {
        coEvery { mockApiService.getTodos(any(), any(), any()) } throws Exception("Network Error")

        viewModel.fetchTodos()
        assert(viewModel.error.value == "Failed to fetch todos")

        coVerify { mockApiService.getTodos(any(), any(), any()) }
    }

    // Additional tests for createTodo, updateTodo, etc.
}
