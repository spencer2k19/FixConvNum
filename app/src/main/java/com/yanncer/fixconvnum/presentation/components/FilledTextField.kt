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
import androidx.compose.material3.TextField
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
fun FilledTextField(
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
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(
                    color = Color.Black.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(size = 20.dp)
                ),
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
                    color = Color.Black.copy(alpha = 0.4f),
                    modifier = Modifier.padding(
                        start = 5.dp
                    ),
                    fontSize = 14.sp,

                    fontWeight = FontWeight.W400

                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,

            colors =  TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,  // Fond quand le champ est activé
                unfocusedContainerColor = Color.Transparent,  // Fond quand le champ est désactivé
                disabledContainerColor = Color.Transparent,  // Fond quand le champ est désactivé
                errorContainerColor = Color.Transparent,     // Fond en cas d'erreur

                focusedIndicatorColor = Color.Transparent,   // Couleur de la bordure activée
                unfocusedIndicatorColor = Color.Transparent, // Couleur de la bordure désactivée
                disabledIndicatorColor = Color.Transparent,  // Couleur de la bordure désactivée
                errorIndicatorColor = Color.Transparent // Bordure rouge en cas d'erreur
            ),

//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                textColor = Color.Black,
//
//                unfocusedLabelColor = Color.Transparent,
//                focusedBorderColor = Color.Transparent, // Couleur du bord lorsqu'il est activé
//                unfocusedBorderColor = Color.Transparent, // Couleur du bord lorsqu'il est désactivé
//                errorBorderColor = Color.Transparent,
//                disabledBorderColor = Color.Transparent,
//
//
//
//            ),



            )

//        if(isError) {
//            Text(text = errorMsg, fontSize = 14.sp,
//
//                fontWeight = FontWeight.W400, color = MaterialTheme.colorScheme.error,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 10.dp, top = 5.dp))
//
//        }

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