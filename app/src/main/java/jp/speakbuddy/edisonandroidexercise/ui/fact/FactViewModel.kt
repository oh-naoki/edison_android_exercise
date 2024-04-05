package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.speakbuddy.edisonandroidexercise.network.FactServiceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FactViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(FactUiState.initial)
    val uiState: StateFlow<FactUiState> = _uiState.asStateFlow()

    fun updateFact() {
        viewModelScope.launch {
            val fact = FactServiceProvider.provide().getFact().fact
            _uiState.value = FactUiState(fact = fact)
        }
    }
}

data class FactUiState(
    val fact: String,
) {
    companion object {
        val initial = FactUiState(fact = "")
    }
}

