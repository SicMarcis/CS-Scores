package cz.sic.ds.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.sic.ds.R
import cz.sic.ds.theme.Ds
import cz.sic.ds.theme.DsColorsLight
import cz.sic.ds.theme.ScoreTheme
import cz.sic.ds.utils.DsPreview

@Composable
fun DsStoreBadge(
    type: BadgeType?,
    modifier: Modifier = Modifier
) {
    Badge(
        modifier = modifier
            .padding(4.dp),
        containerColor = when (type) {
            BadgeType.Local -> Ds.Color.Badge.contentColorLocal
            BadgeType.Remote -> Ds.Color.Badge.contentColorRemote
            null -> Ds.Color.Badge.contentColorLocal
        },
    ) {
        Text(
            style = MaterialTheme.typography.bodyMedium,
            color = Ds.Color.Badge.textColor,
            modifier = modifier
                .padding(4.dp),
            text = when (type) {
                BadgeType.Local -> stringResource(R.string.badge_local)
                BadgeType.Remote -> stringResource(R.string.badge_remote)
                null -> "-"
            })
    }
}

enum class BadgeType {
    Local,
    Remote
}

@DsPreview
@Composable
fun DsStoreBadgePreview() {
    ScoreTheme {
        DsStoreBadge(type = BadgeType.Local)
    }

}