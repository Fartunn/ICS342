package com.example.assignment2

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = ApiService.apiService

    fun loginUser(email: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            try {
                val user = apiService.loginUser("c996c7dd-4fcc-48ce-90c3-bf22321cf290", LoginUserRequest(email, password))
                user?.let {
                    Log.d("LoginViewModel", "Saving User ID: ${it.id}, Token: ${it.token}")
                    SharedPreferencesManager.saveUserId(getApplication(), it.id)
                    SharedPreferencesManager.saveUserToken(getApplication(), it.token)
                }
                onResult(user)
            } catch (e: retrofit2.HttpException) {
                Log.e("Login", "HTTP Error: ${e.code()} ${e.message()}")
                onResult(null)
            } catch (e: Exception) {
                Log.e("Login", "Error logging in: ${e.message}")
                onResult(null)
            }
        }
    }
}
