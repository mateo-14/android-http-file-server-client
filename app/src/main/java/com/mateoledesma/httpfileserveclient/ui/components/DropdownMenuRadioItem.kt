package com.mateoledesma.httpfileserveclient.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


val MIN_RADIO_MENU_ITEM_HEIGHT = 48.dp

@Composable
fun DropdownMenuRadioItem(
    modifier: Modifier = Modifier,
    selected: Boolean,
    text: @Composable () -> Unit,
    onClick: () -> Unit,
    contentPadding: PaddingValues = MenuDefaults.DropdownMenuItemContentPadding,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick)
            .sizeIn(
                minHeight = MIN_RADIO_MENU_ITEM_HEIGHT,
            )
            .padding(contentPadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        text()
        RadioButton(selected = selected, onClick = null)
    }
}