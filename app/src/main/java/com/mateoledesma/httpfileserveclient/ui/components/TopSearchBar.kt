package com.mateoledesma.httpfileserveclient.ui.components

import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp

@Composable
fun SearchTopBar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String? = null,
) {
    var state by remember {
        mutableStateOf(TextRange(value.length))
    }

    BasicTextField(
        modifier = modifier,
        value = TextFieldValue(value, state),
        onValueChange = {
            state = it.selection
            onValueChange(it.text)
        },
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp
        ),
        singleLine = true,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),

        decorationBox = { innerTextField ->
            if (!placeholder.isNullOrEmpty() && value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground.copy(
                            alpha = 0.5f
                        ),
                        fontSize = 18.sp
                    )
                )
            }
            innerTextField()
        }
    )
}