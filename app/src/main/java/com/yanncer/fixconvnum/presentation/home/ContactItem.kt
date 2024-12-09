package com.yanncer.fixconvnum.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactItem() {
    Row(modifier = Modifier.padding(bottom = 20.dp)) {
        Box(modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .background(shape = CircleShape, color = Color.Gray.copy(alpha = 0.4f))

        ) {
            Text(text = "SH", style = TextStyle(
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Color.Black
            ), modifier = Modifier.align(Alignment.Center))
        }

        Spacer(modifier = Modifier.width(10.dp))
        Column(horizontalAlignment = Alignment.Start) {
            Row {
               Text(text = "JOHN", style = TextStyle(
                   fontWeight = FontWeight.W400
               )
               )
                Spacer(modifier = Modifier.width(5.dp))
                Text(text = "DOECHART", style = TextStyle(
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                ))

            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "+229 6161616161, +228 61236323, +229 63616263, 69616161, 64646464", style = TextStyle(
                lineHeight = 20.sp
            ), modifier = Modifier.padding(end = 10.dp)
            )
        }
    }
}

















@Preview
@Composable
fun PrevContactItem() {
    ContactItem()
}