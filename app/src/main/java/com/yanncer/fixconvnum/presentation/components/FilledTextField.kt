package com.yanncer.fixconvnum.presentation.components



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilledTextField(
    text: String,
    onValueChange: (text: String) -> Unit,
    placeHolder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    obscureText: Boolean = false,
    isError: Boolean = false,
    errorMsg: String = "",
) {
    Column {
        Box(modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isSystemInDarkTheme()) Color(0xFF1c1c20) else Color.Black.copy(alpha = 0.05f),
                shape = CircleShape
            )
            .padding(horizontal = 16.dp)
            .height(40.dp),
            contentAlignment = Alignment.Center


        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (leadingIcon != null) {
                    Box(modifier = Modifier.padding(end = 8.dp)) {
                        leadingIcon()
                    }
                }
                BasicTextField(
                    value = text,
                    onValueChange = onValueChange,

                    modifier = Modifier
                        .weight(1f)
                        .padding(start = if (leadingIcon != null) 0.dp else 8.dp, top = 4.dp, bottom = 4.dp),
                    singleLine = true,


                    //                modifier = Modifier
                    //                    .fillMaxWidth()
                    //                    .height(50.dp)
                    //                    .background(
                    //                        color = if (isSystemInDarkTheme()) Color(0xFF1c1c20) else Color.Black.copy(
                    //                            alpha = 0.05f
                    //                        ),
                    //                        shape = CircleShape
                    //                    ),
                    textStyle = TextStyle(
                        color = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                    visualTransformation = if (obscureText) PasswordVisualTransformation() else VisualTransformation.None,



                    ) { innerTextField ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        // Placeholder
                        if (text.isEmpty()) {
                            Text(
                                text = placeHolder,
                                color = if (isSystemInDarkTheme()) Color.White.copy(alpha = 0.5f) else Color.Black.copy(
                                    alpha = 0.4f
                                ),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 2.dp) // Espacement du placeholder
                            )
                        }

                        innerTextField()

                    }
                }

                if (trailingIcon != null) {
                    Box(modifier = Modifier.padding(start = 8.dp)) {
                        trailingIcon()
                    }
                }
            }
        }



    }
}


@Preview
@Composable
fun PrevFilledTextField() {
    var text by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .background(Color.White)
            .padding(20.dp)
    ) {
        FilledTextField(text = text,
            onValueChange = { text = it },
            placeHolder = "Please enter value", isError = true, errorMsg = "An error occured")

    }


}