package cz.sic.scores

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import cz.sic.scores.navigation.ScoresNavGraph

@Composable
fun ScoreMain() {
    val navController = rememberNavController()
    ScoresNavGraph(navController = navController)
}