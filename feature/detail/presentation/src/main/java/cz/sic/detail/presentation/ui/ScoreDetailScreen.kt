package cz.sic.detail.presentation.ui

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.sic.detail.presentation.vm.ScoreDetailContract
import cz.sic.detail.presentation.vm.ScoreDetailViewModel
import cz.sic.domain.model.Score
import cz.sic.ds.components.LoadingContent
import cz.sic.ds.theme.ScoreTheme
import cz.sic.ds.utils.DsPreview
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScoreDetailScreen(
    id: Long,
    viewModel: ScoreDetailViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.onUiAction(ScoreDetailContract.UiAction.LoadScore(id))
    }

    LaunchedEffect(state.events) {
        state.events.forEach { event ->
            when (event) {
                is ScoreDetailContract.UiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    ScoreDetailContent(
        state = state,
        onSave = { /*viewModel.onUiAction(ScoreDetailContract.UiAction.SaveScore(score))*/ }
    )
}

@Composable
fun ScoreDetailContent(
    state: ScoreDetailContract.UiState,
    modifier: Modifier = Modifier,
    onSave: () -> Unit = {}
) {
    Text("MEssage")
    if(state.isLoading) {
        LoadingContent()
    }
}

@DsPreview
@Composable
private fun ScoreDetailContentPreview() {
    ScoreTheme {
        ScoreDetailContent(
            state = ScoreDetailContract.UiState(
                isLoading = true,
                score = Score(
                    id = 1,
                    name = "Name",
                    address = "2024-01-01",
                    duration = 666,
                )
            ),
            onSave = { }
        )
    }

}