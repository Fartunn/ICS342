package com.example.assignment2

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class TodoListViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = ApiService.apiService
    private val _todos = MutableLiveData<List<TodoItem>>()
    val todos: LiveData<List<TodoItem>> get() = _todos
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    init {
        fetchTodos()
    }

    fun fetchTodos() {
        val userId = SharedPreferencesManager.getUserId(getApplication())
        val token = SharedPreferencesManager.getUserToken(getApplication())

        if (userId == null || token == null) {
            _error.postValue("User ID or token is null. Cannot fetch todos.")
            return
        }

        viewModelScope.launch {
            try {
                val todosList = apiService.getTodos(
                    userId = userId,
                    bearerToken = "Bearer $token",
                    apiKey = "c996c7dd-4fcc-48ce-90c3-bf22321cf290"
                )
                _todos.postValue(todosList)
            } catch (e: Exception) {
                _error.postValue("Failed to fetch todos: ${e.message}")
            }
        }
    }

    fun createTodo(description: String) {
        val userId = SharedPreferencesManager.getUserId(getApplication())
        val token = SharedPreferencesManager.getUserToken(getApplication())

        if (userId == null || token == null) {
            _error.postValue("User ID or token is null. Cannot create todo.")
            return
        }

        viewModelScope.launch {
            try {
                val createTodoRequest = CreateTodoRequest(description)
                val newTodo = apiService.createTodo(
                    userId = userId,
                    apiKey = "c996c7dd-4fcc-48ce-90c3-bf22321cf290",
                    bearerToken = "Bearer $token",
                    request = createTodoRequest
                )
                _todos.postValue(_todos.value?.plus(newTodo))
            } catch (e: Exception) {
                _error.postValue("Failed to create todo: ${e.message}")
            }
        }
    }

    fun completeTodo(todoId: Int, description: String, completed: Boolean) {
        updateTodo(todoId, description, completed)
    }

    fun updateTodo(todoId: Int, description: String, completed: Boolean) {
        val userId = SharedPreferencesManager.getUserId(getApplication())
        val token = SharedPreferencesManager.getUserToken(getApplication())

        if (userId == null || token == null) {
            _error.postValue("User ID or token is null. Cannot update todo.")
            return
        }

        viewModelScope.launch {
            try {
                val updateTodoRequest = UpdateTodoRequest(description, completed)
                val updatedTodo = apiService.editTodo(
                    userId = userId,
                    todoId = todoId,
                    bearerToken = "Bearer $token",
                    apiKey = "c996c7dd-4fcc-48ce-90c3-bf22321cf290",
                    request = updateTodoRequest
                )
                _todos.postValue(_todos.value?.map {
                    if (it.id == todoId) updatedTodo else it
                })
            } catch (e: Exception) {
                _error.postValue("Failed to update todo: ${e.message}")
            }
        }
    }

    fun clearError() {
        _error.postValue(null)
    }
}
