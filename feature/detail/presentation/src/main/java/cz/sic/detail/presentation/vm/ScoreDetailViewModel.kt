package cz.sic.detail.presentation.vm

import androidx.lifecycle.viewModelScope
import cz.sic.detail.domain.usecase.ChangeScoresUseCase
import cz.sic.detail.presentation.model.Mode
import cz.sic.domain.model.Score
import cz.sic.domain.model.ScoreWithStore
import cz.sic.domain.model.Store
import cz.sic.domain.usecase.GetScoreItemUseCase
import cz.sic.utils.BaseViewModel
import cz.sic.utils.UiStateAware
import cz.sic.utils.fold
import kotlinx.coroutines.launch

class ScoreDetailViewModel(
    val getScoreItemUseCase: GetScoreItemUseCase,
    val changeScoresUseCase: ChangeScoresUseCase
): BaseViewModel<
        ScoreDetailContract.UiAction,
        ScoreDetailContract.UiEvent,
        ScoreDetailContract.UiData
        >(
    initialState = UiStateAware.UiState(
        isLoading = true,
        uiData = ScoreDetailContract.UiData()
    )
) {

    override suspend fun handleUiAction(action: ScoreDetailContract.UiAction) {
        when (action) {
            is ScoreDetailContract.UiAction.LoadScore -> loadScore(action.id!!, action.store!!)
            is ScoreDetailContract.UiAction.SaveScore -> saveScore()
            is ScoreDetailContract.UiAction.AddScore -> handleAddScore()
            is ScoreDetailContract.UiAction.ValueChange<*> -> updateValue(action)
        }
    }

    private fun handleAddScore() {
        updateUi(
            isLoading = false,
            uiData = {
                it.copy(
                    editEnabled = true,
                    score = ScoreWithStore(
                        score = Score(
                            name = "",
                            address = "",
                            duration = 0,
                        ),
                        store = Store.Any
                    ),
                    mode = Mode.Add
                )
            }
        )
    }
    private fun updateValue(change: ScoreDetailContract.UiAction.ValueChange<*>) {
        when (change) {
            is ScoreDetailContract.UiAction.ValueChange.Name -> {
                updateUi(
                    isLoading = false,
                    uiData = { data ->
                        val current = data.score
                        current?.let {
                            data.copy(
                                score = current.copy(
                                    score = current.score.copy(
                                        name = change.value
                                    )
                                )
                            )
                        } ?: data
                    }
                )
            }
            is ScoreDetailContract.UiAction.ValueChange.Address -> {
                updateUi(
                    isLoading = false,
                    uiData = { data ->
                        val current = data.score
                        current?.let {
                            data.copy(
                                score = current.copy(
                                    score = current.score.copy(
                                        address = change.value
                                    )
                                )
                            )
                        } ?: data
                    }
                )
            }
            is ScoreDetailContract.UiAction.ValueChange.Duration -> {
                updateUi(
                    isLoading = false,
                    uiData = { data ->
                        val current = data.score
                        val duration = change.value.toIntOrNull() ?: 0
                        current?.let {
                            data.copy(
                                score = current.copy(
                                    score = current.score.copy(
                                        duration = duration
                                    )
                                )
                            )
                        } ?: data
                    }
                )
            }
            is ScoreDetailContract.UiAction.ValueChange.Location -> {
                updateUi(
                    isLoading = false,
                    uiData = { data ->
                        val current = data.score
                        current?.let {
                            data.copy(
                                score = current.copy(
                                    store = change.value,
                                )
                            )
                        } ?: data
                    }
                )
            }
        }
    }
    private fun loadScore(id: Long, store: Store) {
        updateUi(
            isLoading = true,
            uiData = { it.copy(editEnabled = false) }
        )
        viewModelScope.launch {
            getScoreItemUseCase(id, store)
                .fold(
                    success = { result ->
                        updateUi(
                            isLoading = false,
                            uiData = {
                                it.copy(
                                    score = result
                                )
                            }
                        )
                    },
                    error = { error ->
                        updateUiEvents(
                            isLoading = false,
                            uiEvents = {
                                it + ScoreDetailContract.UiEvent.ShowError(error.throwable?.message ?: "Error loading score")
                            }
                        )
                    }
                )
        }
    }

    private fun saveScore() {

        val score = _uiState.value.uiData.score
        val store = score?.store
        if(score == null || store == null) {
            updateUiEvents(
                isLoading = false,
                uiEvents = {
                    it + ScoreDetailContract.UiEvent.ShowError("Input form not valid")
                }
            )
            return
        }
        viewModelScope.launch {
            updateIsLoading(true)
            runCatching { changeScoresUseCase.saveScore(score.score, store) }
                .fold(
                    onSuccess = {
                        updateUiEvents(
                            isLoading = false,
                            uiEvents = { it + ScoreDetailContract.UiEvent.ScoreSaved }
                        )
                    },
                    onFailure = { t ->
                        updateUiEvents(
                            isLoading = false,
                            uiEvents = { it + ScoreDetailContract.UiEvent.ShowError("Error saving: ${t.message}") }
                        )
                    }
                )
        }
    }
}