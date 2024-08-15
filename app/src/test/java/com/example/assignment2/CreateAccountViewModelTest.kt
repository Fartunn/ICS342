package com.example.assignment2

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.every
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.Test
import retrofit2.Response

class CreateAccountViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mockApi = mockk<TodoApiService>()  // Mock the service used in your ViewModel
    private lateinit var viewModel: CreateAccountViewModel

    @Before
    fun setUp() {
        val application = Application()  // Use a plain Application instance
        viewModel = CreateAccountViewModel(application)
        mockkObject(ApiService)
        every { ApiService.apiService } returns mockApi
    }

    @Test
    fun `registerUser() should assign User object on successful registration`() = runTest {

        val mockUser = User("123", "token123")
        coEvery { mockApi.registerUser(any(), any()) } returns mockUser  // Use User directly if the method does not return Response<User>

        viewModel.createAccount("test@example.com", "Test User", "password123") { user ->
            assertEquals(mockUser, user)
        }

        coVerify { mockApi.registerUser(any(), any()) }
    }

    @Test
    fun `registerUser() should not assign User object on failed registration`() = runTest {

        coEvery { mockApi.registerUser(any(), any()) } throws Exception("Network Error")

        viewModel.createAccount("test@example.com", "Test User", "password123") { user ->
            assertNull(user)
        }

        coVerify { mockApi.registerUser(any(), any()) }
    }
}
