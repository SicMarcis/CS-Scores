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
import cz.sic.ds.utils.DsPreview
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScoreDetailScreen(
    viewModel: ScoreDetailViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onUiAction(ScoreDetailContract.UiAction.OnAppear)
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

    ScoreDetailContent()
}

@Composable
fun ScoreDetailContent(
    modifier: Modifier = Modifier,
    onSave: () -> Unit = {}
) {
    Text("MEssage")
}

@DsPreview
@Composable
private fun ScoreDetailContentPreview() {
    /*ScoreTheme {
        ScoreDetailContent(
            onSave = { }
        )
    }*/

}