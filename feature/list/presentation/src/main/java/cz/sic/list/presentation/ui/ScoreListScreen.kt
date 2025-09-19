package cz.sic.list.presentation.ui

import android.widget.ProgressBar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.sic.ds.theme.Ds
import cz.sic.ds.theme.ScoreTheme
import cz.sic.ds.utils.DsPreview
import cz.sic.list.domain.model.Score
import cz.sic.list.domain.model.ScoreWithStore
import cz.sic.list.domain.model.Store
import cz.sic.list.presentation.R
import cz.sic.list.presentation.vm.ScoresListContract
import cz.sic.list.presentation.vm.ScoresListViewModel
import org.koin.androidx.compose.koinViewModel

//@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreListScreen(
    viewModel: ScoresListViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState,
    onNavigateToDetail: (Long) -> Unit,
    onNavigateToAddScore: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.onUiAction(ScoresListContract.UiAction.OnAppear)
    }

    LaunchedEffect(state.events) {
        state.events.forEach { event ->
            when (event) {
                is ScoresListContract.UiEvent.ShowError -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is ScoresListContract.UiEvent.ShowDetail -> {
                    snackbarHostState.showSnackbar("Clicked score: ${event.id}")
                    onNavigateToDetail(event.id)
                }
                ScoresListContract.UiEvent.ShowAddScreen -> onNavigateToAddScore()
            }
        }
    }

    ScoreListContent(
        uiState = state,
        onItemClick = { viewModel.onUiAction(ScoresListContract.UiAction.OnScoreClick(it)) },
        onStoreSelected = { viewModel.onUiAction(ScoresListContract.UiAction.OnStoreSelect(it))},
        onAddScoreClick = { viewModel.onUiAction(ScoresListContract.UiAction.OnAddScoreClick) }
    )


}

@Composable
fun ScoreListContent(
    uiState: ScoresListContract.UiState,
    modifier: Modifier = Modifier,
    onItemClick: (Long) -> Unit,
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
            ScoreList(
                uiState.scores,
                onClick = {
                    onItemClick(it)
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
            LoadingContent(
                modifier = Modifier
                    .align(Alignment.Center)

            )
        }
    }



}

@Composable
fun ScoreList(
    scores: List<ScoreWithStore>,
    onClick: (Long) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp)
    ) {
        itemsIndexed(scores, key = { _, item ->  item.score.id }) { index, item ->
            if(index > 0) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            ScoreItem(
                item = item,
                onClick = { onClick(item.score.id) }
            )
        }
    }
}
@Composable
fun ScoreItem(
    item: ScoreWithStore,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Ds.Color.Theme.itemBackground
        ),
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = item.score.name)
                Text(text = "Address: ${item.score.address}")
                Row{
                    Text(text = "Duration: ${item.score.duration}",)
                }
            }
            StoreBadge(
                store = item.store,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
            )

        }

    }

}

@Composable
fun StoreBadge(
    store: Store,
    modifier: Modifier = Modifier
) {
    Badge(
        modifier = modifier,
        containerColor = when (store) {
            Store.Local -> Ds.Color.Badge.contentColorLocal
            Store.Remote -> Ds.Color.Badge.contentColorRemote
            Store.Any -> Ds.Color.Badge.contentColorLocal
        }
    ) {
        Text(text = when (store) {
            Store.Local -> "Local"
            Store.Remote -> "Remote"
            Store.Any -> "Unknown"
        })
    }
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

@Composable
fun LoadingContent(
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(100.dp),
        color = Ds.Color.Transparent.t50,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@DsPreview
@Composable
fun ScorePreview() {
    ScoreTheme {
        ScoreItem(
            item =
                ScoreWithStore(
                    score = Score(
                        id = 1,
                        name = "Test score",
                        address = "Test address",
                        duration = 1234
                    ),
                    store = Store.Remote
                ),
            onClick = {  }
        )
    }
}

@DsPreview
@Composable
fun ScoreListPreview() {
    ScoreTheme {
        ScoreList(
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
            onClick = {  }
        )
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
            onItemClick = { },
            onStoreSelected = { },
            onAddScoreClick = { }
        )
    }
}

@DsPreview
@Composable
private fun LoadingContentPreview() {
    ScoreTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            LoadingContent()
        }

    }
}
