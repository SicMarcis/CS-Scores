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
import cz.sic.ds.components.DsLoadingContent
import cz.sic.ds.components.DsRowRadioButton
import cz.sic.ds.components.DsTextField
import cz.sic.ds.components.DsStoreBadge
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
    onBack: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        when (mode) {
            Mode.View -> viewModel.onUiAction(ScoreDetailContract.UiAction.LoadScore(id, store, mode))
            Mode.Add -> viewModel.onUiAction(ScoreDetailContract.UiAction.AddScore)
        }
    }

    LaunchedEffect(state.events) {
        state.events.forEach { event ->
            viewModel.onUiEventConsumed(event)
            when (event) {
                is ScoreDetailContract.UiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                ScoreDetailContract.UiEvent.ScoreSaved -> onBack()
            }
        }
    }

    ScoreDetailContent(
        state = state,
        onNameChanged = {
            viewModel.onUiAction(ScoreDetailContract.UiAction.ValueChange.Name(it))
        },
        onAddressChanged = {
            viewModel.onUiAction(ScoreDetailContract.UiAction.ValueChange.Address(it))
        },
        onDurationChanged = {
            viewModel.onUiAction(ScoreDetailContract.UiAction.ValueChange.Duration(it))
        },
        onStoreChanged = {
            viewModel.onUiAction(ScoreDetailContract.UiAction.ValueChange.Location(it))
        },
        onSaveClicked = {
            viewModel.onUiAction(ScoreDetailContract.UiAction.SaveScore)
        }
    )
}

@Composable
fun ScoreDetailContent(
    state: ScoreDetailContract.UiState,
    onNameChanged: (String) -> Unit = {},
    onAddressChanged: (String) -> Unit = {},
    onDurationChanged: (String) -> Unit = {},
    onStoreChanged: (Store) -> Unit = {},
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
            DsTextField(
                value = score.score.name,
                label = "Name",
                onValueChanged = onNameChanged,
                enabled = state.editEnabled,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            DsTextField(
                value = score.score.address,
                label = "Address",
                onValueChanged = onAddressChanged,
                enabled = state.editEnabled,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            DsTextField(
                value = score.score.duration.toString(),
                label = "Duration",
                onValueChanged = onDurationChanged,
                enabled = state.editEnabled,
                modifier = Modifier.fillMaxWidth()
            )

            when (score.store) {
                Store.Local -> BadgeType.Local
                Store.Remote -> BadgeType.Remote
                Store.Any -> null
            }?.let {
                Spacer(Modifier.height(16.dp))
                DsStoreBadge(
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
                DsRowRadioButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = stringResource(cz.sic.ds.R.string.badge_local) ,
                    selected = state.score.store == Store.Local,
                    onSelected = { onStoreChanged(Store.Local) }
                )
                DsRowRadioButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = stringResource(cz.sic.ds.R.string.badge_remote),
                    selected = state.score.store == Store.Remote,
                    onSelected = { onStoreChanged(Store.Remote) }
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
            DsLoadingContent(
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