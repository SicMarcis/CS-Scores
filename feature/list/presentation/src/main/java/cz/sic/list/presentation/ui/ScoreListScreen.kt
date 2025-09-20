package cz.sic.list.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
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
import cz.sic.domain.model.Score
import cz.sic.domain.model.Store
import cz.sic.ds.components.BadgeType
import cz.sic.ds.components.DsLoadingContent
import cz.sic.ds.components.ScoreItem
import cz.sic.ds.components.ScoreList
import cz.sic.ds.theme.ScoreTheme
import cz.sic.ds.utils.DsPreview
import cz.sic.domain.model.ScoreWithStore
import cz.sic.list.presentation.R
import cz.sic.list.presentation.vm.ScoresListContract
import cz.sic.list.presentation.vm.ScoresListViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreListScreen(
    viewModel: ScoresListViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigateToDetail: (Long, Store) -> Unit,
    onNavigateToAddScore: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onUiAction(ScoresListContract.UiAction.OnAppear)
    }

    LaunchedEffect(state.events) {
        state.events.forEach { event ->
            viewModel.onUiEventConsumed(event)
            when (event) {
                is ScoresListContract.UiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is ScoresListContract.UiEvent.ShowDetail -> {
                    onNavigateToDetail(event.id, event.store)
                }
                ScoresListContract.UiEvent.ShowAddScreen -> onNavigateToAddScore()
            }
        }
    }

    ScoreListContent(
        uiState = state,
        onItemClick = { id, store -> viewModel.onUiAction(ScoresListContract.UiAction.OnScoreClick(id, store)) },
        onStoreSelected = { viewModel.onUiAction(ScoresListContract.UiAction.OnStoreSelect(it))},
        onAddScoreClick = { viewModel.onUiAction(ScoresListContract.UiAction.OnAddScoreClick) }
    )
}

@Composable
fun ScoreListContent(
    uiState: ScoresListContract.UiState,
    modifier: Modifier = Modifier,
    onItemClick: (Long, Store) -> Unit,
    onStoreSelected: (Store) -> Unit,
    onAddScoreClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .padding(8.dp)

        ) {
            StoreFilter(
                selectedStore = uiState.selectedStore,
                onStoreSelected = { onStoreSelected(it) }
            )
            Spacer(modifier = Modifier
                .height(16.dp)
                .padding(8.dp))
            ScoreList (
                uiState.scores.map { it.toScoreItem() },
                onClick = { item ->
                    val store = when (item.badgeType) {
                        BadgeType.Local -> Store.Local
                        BadgeType.Remote -> Store.Remote
                        null -> null
                    }
                    store?.let { onItemClick(item.id, store) }
                }
            )
        }
        FloatingActionButton(
            onClick = { onAddScoreClick() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
                .wrapContentSize(),
            shape = FloatingActionButtonDefaults.largeShape
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Scroll to top",
            )
        }

        if(uiState.isLoading) {
            DsLoadingContent(
                modifier = Modifier
                    .align(Alignment.Center)

            )
        }
    }
}

fun ScoreWithStore.toScoreItem(): ScoreItem {
    return ScoreItem(
        id = this.score.id,
        name = this.score.name,
        address = this.score.address,
        duration = this.score.duration,
        badgeType = when (this.store) {
            Store.Local -> BadgeType.Local
            Store.Remote -> BadgeType.Remote
            Store.Any -> null
        }
    )
}

@Composable
fun StoreFilter(
    selectedStore: Store,
    onStoreSelected: (Store) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        Store.entries
            .reversed()
            .forEachIndexed { index, store ->
            val isSelected = store == selectedStore
            SegmentedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onStoreSelected(store) },
                selected = isSelected,
                shape = SegmentedButtonDefaults.itemShape(index, 3),
            ) {
                Text(when (store) {
                    Store.Local -> stringResource(R.string.store_local)
                    Store.Remote -> stringResource(R.string.store_remote)
                    Store.Any -> stringResource(R.string.store_any)
                })
            }
        }
    }
}

@DsPreview
@Composable
private fun ScoreListContentPreview() {
    ScoreTheme {
        ScoreListContent(
            uiState = ScoresListContract.UiState(
                isLoading = true,
                scores = listOf(
                    ScoreWithStore(
                        Score(
                            id = 1,
                            name = "Test score",
                            address = "Test address",
                            duration = 1234
                        ),
                        store = Store.Local
                    ),
                    ScoreWithStore(
                        score = Score(
                            id = 2,
                            name = "Test score",
                            address = "Test address",
                            duration = 1234
                        ),
                        store = Store.Local
                    )

                ),
                selectedStore = Store.Any,
                events = emptyList()
            ),
            onItemClick = {_,_ -> },
            onStoreSelected = { },
            onAddScoreClick = { }
        )
    }
}