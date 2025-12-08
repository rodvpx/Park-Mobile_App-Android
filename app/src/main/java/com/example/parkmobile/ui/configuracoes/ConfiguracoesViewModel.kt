package com.example.parkmobile.ui.configuracoes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

sealed class ConfiguracoesState {
    object Loading : ConfiguracoesState()
    data class ProfileLoaded(val profile: UserProfile) : ConfiguracoesState()
    object PasswordChanged : ConfiguracoesState()
    data class Error(val message: String) : ConfiguracoesState()
    object LoggedOut : ConfiguracoesState()
}

class ConfiguracoesViewModel(private val repository: ConfiguracoesRepository) : ViewModel() {

    private val _state = MutableLiveData<ConfiguracoesState>()
    val state: LiveData<ConfiguracoesState> = _state

    fun loadUserProfile() {
        viewModelScope.launch {
            _state.value = ConfiguracoesState.Loading
            repository.getUserProfile()
                .onSuccess { _state.value = ConfiguracoesState.ProfileLoaded(it) }
                .onFailure { _state.value = ConfiguracoesState.Error("Falha ao carregar perfil: ${it.message}") }
        }
    }

    fun changePassword(current: String, new: String, confirm: String) {
        if (new != confirm) {
            _state.value = ConfiguracoesState.Error("As novas senhas não conferem")
            return
        }
        viewModelScope.launch {
            _state.value = ConfiguracoesState.Loading
            repository.changePassword(current, new)
                .onSuccess { _state.value = ConfiguracoesState.PasswordChanged }
                .onFailure { _state.value = ConfiguracoesState.Error("Falha ao alterar senha: ${it.message}") }
        }
    }

    fun logout() {
        repository.logout()
        _state.value = ConfiguracoesState.LoggedOut
    }
}
