package com.yanncer.fixconvnum.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yanncer.fixconvnum.presentation.ui.theme.AccentColor
import com.yanncer.fixconvnum.presentation.ui.theme.AccentDarkColor

@Composable
fun CustomFilledButton(
    text:String,
    onClick:()->Unit,
    color: Color = AccentColor,
    textColor: Color = Color.White,
    isLoading: Boolean = false,
    isEnabled: Boolean = true

) {



    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        enabled = isEnabled,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = color, shape = RoundedCornerShape(10.dp))
            .height(50.dp),
        border = BorderStroke(width = 1.dp,color = AccentColor.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(10.dp)

    ) {
        if(isLoading) {
            CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp,
                modifier = Modifier.width(24.dp).height(24.dp))
        } else {
            Text(text = text, color = textColor, style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            ))
        }

    }
}

@Preview
@Composable
fun PrevFilledButton() {
    CustomFilledButton(text = "Test", onClick = {}, isLoading = false)
}