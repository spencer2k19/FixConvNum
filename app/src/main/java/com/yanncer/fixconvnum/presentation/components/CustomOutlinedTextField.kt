package com.yanncer.fixconvnum.presentation.components



import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun CustomOutlinedTextField(
    text: String,
    onValueChange: (text: String) -> Unit,
    placeHolder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    obscureText: Boolean = false,
    isError: Boolean = false,
    errorMsg: String = ""
) {
    Column {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(
                    width = 1.dp,
                    color = Color(0x1A181E22),
                    shape = RoundedCornerShape(12.dp)
                )
                .background(color = Color(0xFFF9F9F9), shape = RoundedCornerShape(size = 12.dp)),
            value = text,
            onValueChange = onValueChange,
            textStyle = TextStyle(
                color = Color.Black
            ),
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (obscureText) PasswordVisualTransformation() else VisualTransformation.None,
            placeholder = {
                Text(
                    text = placeHolder,
                    color = Color(0xFF94979F),
                    modifier = Modifier.padding(
                        start = 5.dp
                    ),
                    fontSize = 14.sp,

                    fontWeight = FontWeight.W400

                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
//            colors = TextFieldColors(
//                textColor = Color.Black,
//                unfocusedLabelColor = Color.Unspecified,
//                focusedBorderColor = Color.Unspecified, // Couleur du bord lorsqu'il est activé
//                unfocusedBorderColor = Color.Unspecified, // Couleur du bord lorsqu'il est désactivé
//                errorBorderColor = Color.Unspecified
//            ),
            shape = RoundedCornerShape(12.dp),


            )

        if(isError) {
            Text(text = errorMsg, fontSize = 14.sp,

                fontWeight = FontWeight.W400, color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, top = 5.dp))

        }

    }
}


@Preview
@Composable
fun PrevOutlinedTextField() {
    var text by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .background(Color.Blue)
            .padding(20.dp)
    ) {
        CustomOutlinedTextField(text = text,
            onValueChange = { text = it },
            placeHolder = "Please enter value", isError = true, errorMsg = "An error occured")

    }


}