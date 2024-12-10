package com.yanncer.fixconvnum.presentation.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yanncer.fixconvnum.presentation.ui.theme.AccentColor

@Composable
fun CustomProgress(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier.width(24.dp).height(24.dp), color = AccentColor,
        strokeWidth = 2.dp)
}


@Preview
@Composable
fun PrevCustomProgress() {
    CustomProgress()
}