package cz.sic.ds.components

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
fun ScoreList(
    scores: List<ScoreItem>,
    onClick: (ScoreItem) -> Unit,
    onLongClick: (ScoreItem) -> Unit = { },
) {
    LazyColumn(
        contentPadding = PaddingValues(4.dp)
    ) {
        itemsIndexed(scores, key = { _, item ->  item.id }) { index, item ->
            if(index > 0) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            DsScoreItem(
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
        ScoreList(
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