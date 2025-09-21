package cz.sic.scores

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import cz.sic.scores.navigation.ScoresNavGraph

@Composable
fun ScoreMain() {
    val navController = rememberNavController()
    ScoresNavGraph(navController = navController)
}