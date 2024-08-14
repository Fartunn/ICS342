package com.example.assignment2

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment2.ApiService
import com.example.assignment2.CreateUserRequest
import com.example.assignment2.SharedPreferencesManager
import com.example.assignment2.User
import kotlinx.coroutines.launch

class CreateAccountViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = ApiService.apiService

    fun createAccount(email: String, name: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            try {
                val request = CreateUserRequest(email, name, password)
                Log.d("CreateAccount", "Request: $request")
                val response = apiService.registerUser("c996c7dd-4fcc-48ce-90c3-bf22321cf290", request)
                Log.d("CreateAccount", "Response: $response")
                response?.let {
                    SharedPreferencesManager.saveUserId(getApplication(), it.id)
                    SharedPreferencesManager.saveUserToken(getApplication(), it.token)
                }
                onResult(response)
            } catch (e: retrofit2.HttpException) {
                Log.e("CreateAccount", "HTTP Error: ${e.code()} ${e.message()}")
                onResult(null)
            } catch (e: Exception) {
                Log.e("CreateAccount", "Error: ${e.message}")
                onResult(null)
            }
        }
    }
}
