package cz.sic.scores.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import cz.sic.detail.presentation.model.Mode
import cz.sic.detail.presentation.ui.ScoreDetailScreen
import cz.sic.domain.model.Store
import cz.sic.list.presentation.ui.ScoreListScreen
import kotlinx.serialization.Serializable

@Serializable
object ScoresListRoute

@Serializable
data class ScoreDetailRoute(
    val id: Long?,
    val store: Store?,
    val mode: Mode
)

@Composable
fun ScoresNavGraph(
    navController: NavHostController,
) {

    NavHost(
        navController = navController,
        startDestination = ScoresListRoute,
    ) {
        composable<ScoresListRoute> {
            ScoreListScreen(
                onNavigateToDetail = { id, store ->
                    navController.navigate(ScoreDetailRoute(id, store, Mode.View))
                },
                onNavigateToAddScore = {
                    navController.navigate(ScoreDetailRoute(null, null, Mode.Add))
                }
            )
        }

        composable<ScoreDetailRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ScoreDetailRoute>()
            ScoreDetailScreen(
                id = route.id,
                store = route.store,
                mode = route.mode,
                onBack = { navController.popBackStack() }
            )
        }
    }
}