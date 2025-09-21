package cz.sic.ds.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.sic.ds.theme.ScoreTheme
import cz.sic.ds.utils.DsPreview

@Composable
fun DsScoreList(
    modifier: Modifier = Modifier,
    scores: List<ScoreItem>,
    onClick: (ScoreItem) -> Unit,
    onLongClick: (ScoreItem) -> Unit = { },
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(4.dp),
    ) {
        itemsIndexed(scores, key = { _, item ->  item.id }) { index, item ->
            if(index > 0) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            DsScoreItem(
                modifier = Modifier.animateItem(
                    fadeInSpec = tween(durationMillis = 500),
                    fadeOutSpec = tween(durationMillis = 500),
                    placementSpec = spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)
                ),
                item = item,
                onClick = { onClick(item) },
                onLongClick = { onLongClick(item) }
            )
        }
    }
}

@DsPreview
@Composable
fun ScoreListPreview() {
    ScoreTheme {
        DsScoreList(
            scores = listOf(
                ScoreItem(
                    id = 1,
                    name = "Test score",
                    address = "Test address",
                    duration = 1234,
                    badgeType = BadgeType.Local
                ),
                ScoreItem(
                    id = 2,
                    name = "Test score",
                    address = "Test address",
                    duration = 1234,
                    badgeType = BadgeType.Remote
                )

            ),
            onClick = {  }
        )
    }
}