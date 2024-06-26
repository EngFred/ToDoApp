package com.engineerfred.kotlin.todoapp.feature_todo.presentation.screens.todo_list_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.engineerfred.kotlin.todoapp.R

@Composable
fun DrawerHeader() {

    val bg = if ( !isSystemInDarkTheme() ) Color(0xFF0061A4) else Color(0xFF1A1C1E)
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.2f)
            .background(bg)
    ) {
        Image(
            painter = painterResource(id = R.drawable.to_do_icon),
            contentDescription = stringResource(id = R.string.logo_image_icon),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(65.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Note It Down!",
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFE2E2E6)
        )
    }
}