package com.example.jcasd.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jcasd.data.local.AppEvent
import com.example.jcasd.data.local.ShowDao
import com.example.jcasd.data.local.ShowEntity
import com.example.jcasd.data.local.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// step 14 create a view model class, rename it to your liking
class AppViewModel(
    private val dao: ShowDao
) : ViewModel() {
    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state

    init {
        viewModelScope.launch {
            dao.getShows().collect {
                _state.update{
                    it.copy(shows = it.shows)
                }
            }
        }
    }

    fun onEvent(event: AppEvent) {
        when (event) {
            AppEvent.SaveShow -> saveShow()
            AppEvent.DismissDialog -> _state.update { it.copy(isDialogVisible = false) }
            AppEvent.ShowDialog -> _state.update { it.copy(isDialogVisible = true) }
            is AppEvent.SetOtt -> _state.update { it.copy(ott = event.ott) }
            is AppEvent.SetPriority -> _state.update { it.copy(priority = event.priority) }
            is AppEvent.SetTitle -> _state.update { it.copy(title = event.title) }
            is AppEvent.DeleteShow -> viewModelScope.launch {
                dao.deleteShow(event.show)
            }
        }
    }

    private fun saveShow() {
        val title = state.value.title
        val ott = state.value.ott
        val priority = state.value.priority
        if (title.isBlank() || ott.isBlank()) return
        if (priority < 0 || priority > 10) return
        viewModelScope.launch {
            val item = ShowEntity(
                title = title,
                ott = ott,
                priority = priority
            )
            dao.addShow(item)
        }
    }
}