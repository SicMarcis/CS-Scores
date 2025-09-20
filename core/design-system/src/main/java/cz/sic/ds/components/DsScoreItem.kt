package cz.sic.ds.components

import android.widget.Space
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
fun DsScoreItem(
    item: ScoreItem,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {}
) {
    Card(
        shape = CardDefaults.elevatedShape,
        colors = CardDefaults.cardColors(
            containerColor = Ds.Color.Theme.itemBackground
        ),
        modifier = Modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
            .border(
                width = 1.dp,
                color = Ds.Color.Theme.itemBorder,
                shape = RoundedCornerShape(8.dp)
            )
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
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                Row {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = null,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(text = item.address)
                }

                Row{
                    Icon(
                        imageVector = Icons.Rounded.DateRange,
                        contentDescription = null,
                        /*tint = Ds.Color.Theme.iconTint,*/
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = item.duration.toString(),
                    )
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
        DsScoreItem(
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