package cz.sic.detail.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.sic.detail.presentation.R
import cz.sic.detail.presentation.model.Mode
import cz.sic.detail.presentation.vm.ScoreDetailContract
import cz.sic.detail.presentation.vm.ScoreDetailViewModel
import cz.sic.domain.model.Score
import cz.sic.domain.model.ScoreWithStore
import cz.sic.domain.model.Store
import cz.sic.ds.components.BadgeType
import cz.sic.ds.components.DsAppBar
import cz.sic.ds.components.DsLoadingContent
import cz.sic.ds.components.DsRowRadioButton
import cz.sic.ds.components.DsStoreBadge
import cz.sic.ds.components.DsTextField
import cz.sic.ds.components.ToolbarState
import cz.sic.ds.theme.DsTheme
import cz.sic.ds.utils.DsPreview
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScoreDetailScreen(
    id: Long?,
    store: Store?,
    mode: Mode,
    viewModel: ScoreDetailViewModel = koinViewModel(),
    onBack: () -> Unit = {},
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DsAppBar(
                toolbarState = ToolbarState(
                    title = R.string.screen_detail_title,
                    showBack = true
                ),
                onBack = onBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LaunchedEffect(id) {
                when (mode) {
                    Mode.View -> viewModel.onUiAction(ScoreDetailContract.UiAction.LoadScore(id, store, mode))
                    Mode.Add -> viewModel.onUiAction(ScoreDetailContract.UiAction.AddScore)
                }
            }

            LaunchedEffect(state.uiEvents) {
                state.uiEvents.forEach { event ->
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
                isLoading = state.isLoading,
                state = state.uiData,
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
    }
}

@Composable
fun ScoreDetailContent(
    isLoading: Boolean,
    state: ScoreDetailContract.UiData,
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        val modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)

        if(state.mode == Mode.View) {
            ViewContent(
                modifier = modifier,
                score = score
            )
        } else {
            EditContent(
                modifier = modifier,
                score = score,
                onNameChanged = onNameChanged,
                onAddressChanged = onAddressChanged,
                onDurationChanged = onDurationChanged,
            )
        }

        if(state.editEnabled) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    DsRowRadioButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        label = stringResource(cz.sic.ds.R.string.badge_local) ,
                        selected = state.score.store == Store.Local,
                        onSelected = { onStoreChanged(Store.Local) }
                    )
                    DsRowRadioButton(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        label = stringResource(cz.sic.ds.R.string.badge_remote),
                        selected = state.score.store == Store.Remote,
                        onSelected = { onStoreChanged(Store.Remote) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onSaveClicked,
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.button_save),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        if(isLoading) {
            DsLoadingContent(
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@Composable
fun EditContent(
    modifier: Modifier = Modifier,
    score: ScoreWithStore,
    onNameChanged: (String) -> Unit = {},
    onAddressChanged: (String) -> Unit = {},
    onDurationChanged: (String) -> Unit = {},
) {
    Column(
        modifier = modifier,
    ) {
        DsTextField(
            value = score.score.name,
            label = "Name",
            onValueChanged = onNameChanged,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        DsTextField(
            value = score.score.address,
            label = "Address",
            onValueChanged = onAddressChanged,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))
        DsTextField(
            value = score.score.duration.toString(),
            label = "Duration",
            onValueChanged = onDurationChanged,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun  ViewContent(
    modifier: Modifier = Modifier,
    score: ScoreWithStore
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = score.score.name,
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

         Spacer(modifier = Modifier.height(16.dp))

         Row {
             Icon(
                 imageVector = Icons.Filled.LocationOn,
                 contentDescription = null,
                 modifier = Modifier
                     .align(Alignment.CenterVertically)
                     .size(50.dp)
                     .padding(end = 8.dp)
             )
             Text(
                 text = score.score.address,
                 style = MaterialTheme.typography.headlineLarge,
                 modifier = Modifier
                     .fillMaxWidth()
                     .align(Alignment.CenterVertically)
                     .wrapContentHeight()
             )
         }

         Spacer(modifier = Modifier.height(16.dp))

         Row {
             Icon(
                 imageVector = Icons.Filled.DateRange,
                 contentDescription = null,
                 modifier = Modifier
                     .align(Alignment.CenterVertically)
                     .size(50.dp)
                     .padding(end = 8.dp)
             )
             Text(
                 text = score.score.duration.toString(),
                 style = MaterialTheme.typography.headlineLarge,
                 modifier = Modifier
                     .fillMaxWidth()
                     .align(Alignment.CenterVertically)
                     .wrapContentHeight()
             )
         }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@DsPreview
@Composable
fun EditContentPreview() {
    DsTheme {
        EditContent(
            score = ScoreWithStore(
                Score(
                    id = 1,
                    name = "Name",
                    address = "2024-01-01",
                    duration = 666,
                ),
                store = Store.Local
            ),
        )
    }
}

@DsPreview
@Composable
fun ViewContentPreview() {
    DsTheme {
        ViewContent(
            score = ScoreWithStore(
                Score(
                    id = 1,
                    name = "Name",
                    address = "2024-01-01",
                    duration = 666,
                ),
                store = Store.Local
            ),
        )
    }
}

@DsPreview
@Composable
private fun ScoreDetailContentPreview() {
    DsTheme {
        ScoreDetailContent(
            isLoading = true,
            state = ScoreDetailContract.UiData(
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