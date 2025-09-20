package cz.sic.ds.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cz.sic.ds.theme.Ds
import cz.sic.ds.theme.ScoreTheme
import cz.sic.ds.utils.DsPreview

@Composable
fun ScoreItem(
    item: ScoreItem,
    onClick: () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Ds.Color.Theme.itemBackground
        ),
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = item.name)
                Text(text = "Address: ${item.address}")
                Row{
                    Text(text = "Duration: ${item.duration}")
                }
            }
            StoreBadge(
                type = item.badgeType,
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
            )

        }

    }

}

data class ScoreItem(
    val id: Long = System.currentTimeMillis(),
    val name: String,
    val address: String,
    val duration: Int,
    val badgeType: BadgeType?
)

@DsPreview
@Composable
fun ScorePreview() {
    ScoreTheme {
        ScoreItem(
            ScoreItem(
                name = "Test score",
                address = "Test address",
                duration = 1234,
                badgeType = BadgeType.Local,
            ),
            onClick = {  }
        )
    }
}