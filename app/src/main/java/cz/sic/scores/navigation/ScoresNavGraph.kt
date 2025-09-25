package cz.sic.scores.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
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

const val ANIM_DURATION = 400
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ScoresNavGraph(
    navController: NavHostController,
) {

    SharedTransitionLayout(

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

            composable<ScoreDetailRoute>(
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(ANIM_DURATION)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(ANIM_DURATION)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(ANIM_DURATION)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(ANIM_DURATION)
                    )
                }
            ) { backStackEntry ->
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

}