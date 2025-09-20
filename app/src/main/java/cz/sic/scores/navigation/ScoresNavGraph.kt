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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import kotlinx.serialization.Serializable
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import cz.sic.detail.presentation.ui.ScoreDetailScreen
import cz.sic.ds.components.AppBar
import cz.sic.ds.components.ToolbarState
import cz.sic.list.presentation.ui.ScoreListScreen
import cz.sic.scores.R

@Serializable
object ScoresListRoute

@Serializable
data class ScoreDetailRoute(val id: Long)

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
            AppBar(
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
                    onNavigateToDetail = { id ->
                        navController.navigate(ScoreDetailRoute(id))
                    },
                    onNavigateToAddScore = {
                        //navController.navigate(AddScoreRoute())
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

                ScoreDetailScreen(
                    id = backStackEntry.toRoute<ScoreDetailRoute>().id,
                    snackbarHostState = snackbarHostState,
                )
            }
        }
    }
}

