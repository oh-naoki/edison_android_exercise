package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.speakbuddy.edisonandroidexercise.repository.FactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FactViewModel @Inject constructor(
    private val factRepository: FactRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FactUiState.initial)
    val uiState: StateFlow<FactUiState> = _uiState.asStateFlow()

    fun updateFact() {
        factRepository.fetchFact().onEach { fact ->
            _uiState.update {
                it.copy(fact = fact.fact, error = null)
            }
        }.catch {
            _uiState.update {
                it.copy(error = it.error)
            }
        }.launchIn(viewModelScope)
    }
}

data class FactUiState(
    val fact: String,
    val error: Throwable? = null,
) {
    companion object {
        val initial = FactUiState(fact = "")
    }
}

