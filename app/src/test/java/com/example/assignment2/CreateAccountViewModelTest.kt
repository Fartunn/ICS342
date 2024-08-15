package com.example.assignment2

import android.app.Application
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutinesTestExtension::class, InstantExecutorExtension::class)
class CreateAccountViewModelTest {

    private val mockApiService = mockk<TodoApiService>()
    private val mockApplication = mockk<Application>(relaxed = true)
    private val viewModel = CreateAccountViewModel(mockApplication)

    @Test
    fun `createAccount should succeed when API call is successful`() = runTest {
        val mockUser = mockk<User>(relaxed = true)
        coEvery { mockApiService.registerUser(any(), any()) } returns mockUser

        var resultUser: User? = null
        viewModel.createAccount("hajji@gmail.com", "Naima", "Hajji@123") { user ->
            resultUser = user
        }

        coVerify(exactly = 1) { mockApiService.registerUser(any(), any()) }
        assertEquals(mockUser, resultUser)
    }

    @Test
    fun `createAccount should return null when API call fails`() = runTest {
        coEvery { mockApiService.registerUser(any(), any()) } throws Exception("Network error")

        var resultUser: User? = null
        viewModel.createAccount("test@gmail.com", "Test User", "password123") { user ->
            resultUser = user
        }

        coVerify(exactly = 1) { mockApiService.registerUser(any(), any()) }
        assertNull(resultUser)
    }
}
