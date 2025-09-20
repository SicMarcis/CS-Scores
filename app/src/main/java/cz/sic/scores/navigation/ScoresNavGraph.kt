package cz.sic.scores.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import kotlinx.serialization.Serializable
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import cz.sic.detail.presentation.model.Mode
import cz.sic.detail.presentation.ui.ScoreDetailScreen
import cz.sic.domain.model.Store
import cz.sic.ds.components.DsAppBar
import cz.sic.ds.components.ToolbarState
import cz.sic.list.presentation.ui.ScoreListScreen

@Serializable
object ScoresListRoute

@Serializable
data class ScoreDetailRoute(
    val id: Long?,
    val store: Store?,
    val mode: Mode
)

@Serializable
object AddScoreRoute

@Composable
fun ScoresNavGraph(
    navController: NavHostController,
) {
    var toolbarState: ToolbarState by remember {
        mutableStateOf(ToolbarState(
            cz.sic.list.presentation.R.string.screen_list_title,
            false
        ))
    }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            DsAppBar(
                toolbarState = toolbarState,
                onBack = { navController.popBackStack() }
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ScoresListRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<ScoresListRoute> {
                LaunchedEffect(Unit) {
                    toolbarState = ToolbarState(
                        title = cz.sic.list.presentation.R.string.screen_list_title,
                        showBack = false
                    )
                }
                ScoreListScreen(
                    snackbarHostState = snackbarHostState,
                    onNavigateToDetail = { id, store ->
                        navController.navigate(ScoreDetailRoute(id, store, Mode.View))
                    },
                    onNavigateToAddScore = {
                        navController.navigate(ScoreDetailRoute(null, null, Mode.Add))
                    }
                )
            }

            composable<ScoreDetailRoute> { backStackEntry ->
                LaunchedEffect(Unit) {
                    toolbarState = ToolbarState(
                        title = cz.sic.list.presentation.R.string.screen_detail_title,
                        showBack = true
                    )
                }

                val route = backStackEntry.toRoute<ScoreDetailRoute>()
                ScoreDetailScreen(
                    id = route.id,
                    store = route.store,
                    mode = route.mode,
                    snackbarHostState = snackbarHostState,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}

