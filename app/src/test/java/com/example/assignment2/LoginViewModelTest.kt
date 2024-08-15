package com.example.assignment2

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel
    private val mockApiService = mockk<TodoApiService>()

    @Before
    fun setUp() {
        val application = ApplicationProvider.getApplicationContext<Application>()
        viewModel = LoginViewModel(application)
        mockkObject(ApiService)
        every { ApiService.apiService } returns mockApiService
    }

    @Test
    fun `loginUser should succeed with valid credentials`() = runTest {

        val mockUser = User("123", "token")
        coEvery { mockApiService.loginUser(any(), any()) } returns mockUser


        viewModel.loginUser("test@example.com", "password") { user ->
            assert(user != null)
            assert(user?.id == "123")
            assert(user?.token == "token")
        }

        coVerify { mockApiService.loginUser(any(), any()) }
    }

    @Test
    fun `loginUser should fail when an error occurs`() = runTest {

        coEvery { mockApiService.loginUser(any(), any()) } throws Exception("Network Error")


        viewModel.loginUser("test@example.com", "password") { user ->
            assert(user == null)
        }

        coVerify { mockApiService.loginUser(any(), any()) }
    }
}
