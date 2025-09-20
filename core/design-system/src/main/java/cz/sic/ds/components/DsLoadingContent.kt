package cz.sic.ds.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.sic.ds.theme.Ds
import cz.sic.ds.theme.ScoreTheme
import cz.sic.ds.utils.DsPreview

@Composable
fun DsLoadingContent(
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
private fun DsLoadingContentPreview() {
    ScoreTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            DsLoadingContent()
        }

    }
}