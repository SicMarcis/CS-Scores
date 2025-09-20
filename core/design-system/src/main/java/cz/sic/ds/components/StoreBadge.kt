package cz.sic.ds.components

import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.sic.ds.R
import cz.sic.ds.theme.Ds

@Composable
fun StoreBadge(
    type: BadgeType?,
    modifier: Modifier = Modifier
) {
    Badge(
        modifier = modifier,
        containerColor = when (type) {
            BadgeType.Local -> Ds.Color.Badge.contentColorLocal
            BadgeType.Remote -> Ds.Color.Badge.contentColorRemote
            null -> Ds.Color.Badge.contentColorLocal
        }
    ) {
        Text(text = when (type) {
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