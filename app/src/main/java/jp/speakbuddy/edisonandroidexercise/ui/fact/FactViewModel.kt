package jp.speakbuddy.edisonandroidexercise.ui.fact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.speakbuddy.edisonandroidexercise.network.FactResponse
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
                it.copy(fact = fact, error = null)
            }
        }.catch { throwable ->
            _uiState.update {
                it.copy(error = throwable)
            }
        }.launchIn(viewModelScope)
    }
}

data class FactUiState(
    val fact: FactResponse,
    val error: Throwable? = null,
) {
    companion object {
        val initial = FactUiState(fact = FactResponse("", 0))
    }

    // MEMO: モデルに含めるロジックか悩んだがこの画面特有のロジックであると判断し、ViewModel に持たせた
    val isLongFact = fact.length >= 100
    val containsCats = fact.fact.contains("cats", ignoreCase = true)
}

