package com.example.parkmobile.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parkmobile.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun cadastrar(email: String, senha: String, confirmarSenha: String, nome: String, cpf: String) {
        if (senha != confirmarSenha) {
            _authState.value = AuthState.Error("As senhas não conferem.")
            return
        }
        if (email.isBlank() || senha.isBlank() || nome.isBlank() || cpf.isBlank()) {
            _authState.value = AuthState.Error("Todos os campos são obrigatórios.")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            val result = authRepository.cadastrar(email, senha, nome, cpf)
            result.onSuccess {
                _authState.value = AuthState.Authenticated
            }.onFailure {
                _authState.value = AuthState.Error("Falha no cadastro: ${it.message}")
            }
        }
    }

    fun login(email: String, senha: String) {
        if (email.isBlank() || senha.isBlank()) {
            _authState.value = AuthState.Error("E-mail e senha são obrigatórios.")
            return
        }

        _authState.value = AuthState.Loading

        viewModelScope.launch {
            val result = authRepository.login(email, senha)
            result.onSuccess {
                _authState.value = AuthState.Authenticated
            }.onFailure {
                _authState.value = AuthState.Error("Falha no login: ${it.message}")
            }
        }
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    data class Error(val message: String) : AuthState()
}
