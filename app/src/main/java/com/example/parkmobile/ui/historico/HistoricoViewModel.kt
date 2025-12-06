package com.example.parkmobile.ui.historico

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.parkmobile.data.model.HistoricoItem

class HistoricoViewModel : ViewModel() {

    private val repository = HistoricoRepository()

    private val _historicoItems = MutableLiveData<List<HistoricoItem>>()
    val historicoItems: LiveData<List<HistoricoItem>> = _historicoItems

    fun carregarHistorico() {
        // Em uma aplicação real, isso poderia ser uma operação assíncrona
        val items = repository.getHistoricoItems()
        _historicoItems.postValue(items)
    }
}
