package cz.sic.ds.components

import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import cz.sic.ds.utils.DsPreview

@Composable
fun DsTextField(
    value: String,
    label: String,
    enabled: Boolean = true,
    onValueChanged: (String) -> Unit,
    modifier: Modifier
) {
    var localValue by rememberSaveable { mutableStateOf(value) }
    LaunchedEffect(value) { localValue = value }

    TextField(
        value = localValue,
        onValueChange = {
            localValue = it
            onValueChanged(it)
        },
        label = { Text(text = label) },
        textStyle = MaterialTheme.typography.bodyMedium,
        shape = RoundedCornerShape(8.dp),
        enabled = enabled,
        modifier = modifier.border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
    )
}

@DsPreview
@Composable
fun DsTextFieldPreview() {
    DsTextField(
        value = "TextField",
        label = "Label",
        onValueChanged = {},
        modifier = Modifier
    )
}