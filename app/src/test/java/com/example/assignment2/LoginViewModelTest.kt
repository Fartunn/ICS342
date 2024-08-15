package com.example.assignment2

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutinesTestExtension::class, InstantExecutorExtension::class)
class LoginViewModelTest {

    private val mockApiService = mockk<TodoApiService>()
    private val viewModel = LoginViewModel(mockk(relaxed = true))

    @Test
    fun `loginUser should succeed with correct credentials`() = runTest {
        val expectedUser = mockk<User>(relaxed = true)
        coEvery { mockApiService.loginUser(any(), any()) } returns expectedUser

        var resultUser: User? = null
        viewModel.loginUser("hajji@gmail.com", "Hajji@123") { user ->
            resultUser = user
        }

        coVerify(exactly = 1) { mockApiService.loginUser(any(), any()) }
        assertEquals(expectedUser, resultUser)
    }

    @Test
    fun `loginUser should return null with incorrect credentials`() = runTest {
        coEvery { mockApiService.loginUser(any(), any()) } throws Exception("Invalid credentials")

        var resultUser: User? = null
        viewModel.loginUser("test@gmail.com", "wrongpassword") { user ->
            resultUser = user
        }

        coVerify(exactly = 1) { mockApiService.loginUser(any(), any()) }
        assertNull(resultUser)
    }
}
