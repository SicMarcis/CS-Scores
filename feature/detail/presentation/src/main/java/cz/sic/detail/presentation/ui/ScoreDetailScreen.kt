package cz.sic.detail.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.sic.detail.presentation.model.Mode
import cz.sic.detail.presentation.vm.ScoreDetailContract
import cz.sic.detail.presentation.vm.ScoreDetailViewModel
import cz.sic.domain.model.Score
import cz.sic.domain.model.ScoreWithStore
import cz.sic.domain.model.Store
import cz.sic.ds.components.BadgeType
import cz.sic.ds.components.LoadingContent
import cz.sic.ds.components.RowRadioButton
import cz.sic.ds.components.StoreBadge
import cz.sic.ds.theme.ScoreTheme
import cz.sic.ds.utils.DsPreview
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScoreDetailScreen(
    id: Long?,
    store: Store?,
    mode: Mode,
    viewModel: ScoreDetailViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.onUiAction(ScoreDetailContract.UiAction.LoadScore(id, store, mode))
    }

    LaunchedEffect(state.events) {
        state.events.forEach { event ->
            viewModel.onUiEventConsumed(event)
            when (event) {
                is ScoreDetailContract.UiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    ScoreDetailContent(
        state = state
    )
}

@Composable
fun ScoreDetailContent(
    state: ScoreDetailContract.UiState,
    onNameChanged: (String) -> Unit = {},
    onAddressChanged: (String) -> Unit = {},
    onDurationChanged: (String) -> Unit = {},
    onSaveClicked: () -> Unit = {},
) {
    val score = state.score
    if(score == null) {
        return
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp)
    ) {
        
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),

        ) {
            TextField(
                value = score.score.name,
                label = {Text("Name")},
                onValueChange = onNameChanged,
                textStyle = MaterialTheme.typography.bodyMedium,
                enabled = state.editEnabled,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            TextField(
                value = score.score.address,
                label = {Text("Address")},
                onValueChange = onAddressChanged,
                textStyle = MaterialTheme.typography.bodyMedium,
                enabled = state.editEnabled,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            TextField(
                value = score.score.duration.toString(),
                label = {Text("Duration")},
                onValueChange = onDurationChanged,
                textStyle = MaterialTheme.typography.bodyMedium,
                enabled = state.editEnabled,
                modifier = Modifier.fillMaxWidth()
            )

            when (score.store) {
                Store.Local -> BadgeType.Local
                Store.Remote -> BadgeType.Remote
                Store.Any -> null
            }?.let {
                Spacer(Modifier.height(16.dp))
                StoreBadge(
                    type = when (score.store) {
                        Store.Local -> BadgeType.Local
                        Store.Remote -> BadgeType.Remote
                        Store.Any -> null
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if(state.editEnabled) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                RowRadioButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = stringResource(cz.sic.ds.R.string.badge_local) ,
                    selected = false,
                    onSelected = {}
                )
                RowRadioButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = stringResource(cz.sic.ds.R.string.badge_remote),
                    selected = false,
                    onSelected = {}
                )
                Button(
                    onClick = onSaveClicked,
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "save",//stringResource(R.string.button_save),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

        }
        if(state.isLoading) {
            LoadingContent(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@DsPreview
@Composable
private fun ScoreDetailContentPreview() {
    ScoreTheme {
        ScoreDetailContent(
            state = ScoreDetailContract.UiState(
                isLoading = true,
                score = ScoreWithStore(
                    Score(
                        id = 1,
                        name = "Name",
                        address = "2024-01-01",
                        duration = 666,
                    ),
                    store = Store.Local
                ),
                editEnabled = true
            )
        )
    }

}