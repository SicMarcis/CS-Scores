package cz.sic.list.presentation.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.sic.domain.model.Score
import cz.sic.domain.model.ScoreWithStore
import cz.sic.domain.model.Store
import cz.sic.ds.components.BadgeType
import cz.sic.ds.components.DsAppBar
import cz.sic.ds.components.DsLoadingContent
import cz.sic.ds.components.ScoreItem
import cz.sic.ds.components.ScoreList
import cz.sic.ds.components.ToolbarState
import cz.sic.ds.theme.ScoreTheme
import cz.sic.ds.utils.DsPreview
import cz.sic.list.presentation.R
import cz.sic.list.presentation.vm.ScoresListContract
import cz.sic.list.presentation.vm.ScoresListViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScoreListScreen(
    viewModel: ScoresListViewModel = koinViewModel(),
    onNavigateToDetail: (Long, Store) -> Unit,
    onNavigateToAddScore: () -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            DsAppBar(
                toolbarState = ToolbarState(
                    title = R.string.screen_list_title,
                    showBack = false
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {  paddingValues ->

        LaunchedEffect(viewModel) {
            viewModel.onUiAction(ScoresListContract.UiAction.OnAppear)
        }

        LaunchedEffect(state.uiEvents) {
            state.uiEvents.forEach { event ->
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

        LaunchedEffect(Unit) {
            snackbarHostState.showSnackbar("clicked")
        }
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            ScoreListContent(
                isLoading = state.isLoading,
                uiState = state.uiData,
                onItemClick = { id, store -> viewModel.onUiAction(ScoresListContract.UiAction.OnScoreClick(id, store)) },
                onItemLongClick = { id, store -> viewModel.onUiAction(ScoresListContract.UiAction.OnDeleteClick(id, store)) },
                onStoreSelected = { viewModel.onUiAction(ScoresListContract.UiAction.OnStoreSelect(it))},
                onAddScoreClick = { viewModel.onUiAction(ScoresListContract.UiAction.OnAddScoreClick) }
            )
        }
    }
}

@Composable
fun ScoreListContent(
    isLoading: Boolean,
    uiState: ScoresListContract.UiData,
    modifier: Modifier = Modifier,
    onItemClick: (Long, Store) -> Unit,
    onItemLongClick: (Long, Store) -> Unit,
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

            if (uiState.scores.isEmpty() && !isLoading) {
                EmptyContent()
            } else {
                ScoreList (
                    uiState.scores.map { it.toScoreItem() },
                    onClick = { item ->
                        val store = when (item.badgeType) {
                            BadgeType.Local -> Store.Local
                            BadgeType.Remote -> Store.Remote
                            null -> null
                        }
                        store?.let { onItemClick(item.id, store) }
                    },
                    onLongClick = { item ->
                        val store = when (item.badgeType) {
                            BadgeType.Local -> Store.Local
                            BadgeType.Remote -> Store.Remote
                            null -> null
                        }
                        store?.let { onItemLongClick(item.id, store) }
                    }
                )
            }

        }
        FloatingActionButton(
            onClick = { onAddScoreClick() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
                .wrapContentSize(),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            shape = FloatingActionButtonDefaults.largeShape
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Scroll to top",
            )
        }

        if(isLoading) {
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
fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = stringResource(R.string.no_scores),
            style = MaterialTheme.typography.headlineLarge
        )
    }
}

@DsPreview
@Composable
fun EmptyContentPreview() {
    ScoreTheme {
        EmptyContent()
    }
}

@DsPreview
@Composable
private fun ScoreListContentPreview() {
    ScoreTheme {
        ScoreListContent(
            isLoading = true,
            uiState = ScoresListContract.UiData(
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
                selectedStore = Store.Any
            ),
            onItemClick = {_,_ -> },
            onStoreSelected = { },
            onAddScoreClick = { },
            onItemLongClick = {_,_ -> }
        )
    }
}