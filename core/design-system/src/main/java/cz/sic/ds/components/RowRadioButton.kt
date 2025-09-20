package cz.sic.ds.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.sic.ds.theme.ScoreTheme
import cz.sic.ds.utils.DsPreview

@Composable
fun RowRadioButton(
    label: String,
    selected: Boolean = false,
    onSelected: () -> Unit = {},
    modifier: Modifier,
) {
    Row(
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = onSelected
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelected

        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@DsPreview
@Composable
fun RowRadioButtonPreview() {
    ScoreTheme {
        RowRadioButton(
            label = "label",
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}