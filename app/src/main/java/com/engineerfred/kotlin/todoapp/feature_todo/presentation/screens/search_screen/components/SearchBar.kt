package com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.search_screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import com.engineerfred.kotlin.todoapp.R
import com.engineerfred.kotlin.todoapp.feature_todo.presentation.theme.ToDoAppTheme

@Composable
fun SearchBar(
    value: String,
    backgroundColor: Color,
    showCloseIcon: Boolean,
    onValueChange:(String) -> Unit,
    onClearClicked:() -> Unit,
    onBackClicked:() -> Unit,
    modifier: Modifier = Modifier
) {

    val localFM = LocalFocusManager.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(backgroundColor),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onBackClicked.invoke() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = stringResource(id = R.string.back_icon),
                tint = Color(0xFFFFFFFF)
            )
        }
        TextField(
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            modifier = modifier.weight(1f),
            placeholder = { Text(text = "Search for task by title", color = Color(0xFFFFDCBE)) },
            textStyle = TextStyle.Default.copy(
                fontSize = 15.sp,
                color = Color(0xFFFFDCBE)
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions { localFM.clearFocus() },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Color(0xFFFFFFFF)
        ))
        AnimatedVisibility(visible = showCloseIcon) {
            IconButton(onClick = { onClearClicked.invoke() }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_close),
                    contentDescription = stringResource(id = R.string.close_icon),
                    tint = Color(0xFFFFFFFF)
                )
            }
        }
    }

}


@Preview( showBackground = true )
@Composable
fun SearchBarPreview() {
    ToDoAppTheme {
        SearchBar(
            value = "",
            backgroundColor = Color(0xFF0061A4),
            onValueChange = {},
            onClearClicked = { /*TODO*/ },
            onBackClicked = { /*TODO*/ },
            showCloseIcon = false
        )
    }
}