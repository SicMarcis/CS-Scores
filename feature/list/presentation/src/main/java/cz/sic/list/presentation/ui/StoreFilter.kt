package cz.sic.list.presentation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import cz.sic.domain.model.Store
import cz.sic.list.presentation.R

@Composable
fun StoreFilter(
    selectedStore: Store,
    onStoreSelected: (Store) -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        Store.entries
            .reversed()
            .forEachIndexed { index, store ->
                val isSelected = store == selectedStore
                SegmentedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { onStoreSelected(store) },
                    selected = isSelected,
                    shape = SegmentedButtonDefaults.itemShape(index, 3),
                ) {
                    Text(when (store) {
                        Store.Local -> stringResource(R.string.store_local)
                        Store.Remote -> stringResource(R.string.store_remote)
                        Store.Any -> stringResource(R.string.store_any)
                    })
                }
            }
    }
}