package cz.sic.scores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.compose.rememberNavController
import cz.sic.ds.theme.ScoreTheme
import cz.sic.list.presentation.ui.ScoreListScreen
import cz.sic.scores.navigation.ScoresNavGraph

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ScoreTheme {
                val navController = rememberNavController()
                ScoresNavGraph(
                    navController = navController
                )
            }
        }
    }
}