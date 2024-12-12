package com.yanncer.fixconvnum.presentation.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yanncer.fixconvnum.R
import com.yanncer.fixconvnum.common.BeninPhoneValidator.hasPhoneNumberIssue
import com.yanncer.fixconvnum.domain.models.Contact
import com.yanncer.fixconvnum.domain.models.PhoneNumber
import com.yanncer.fixconvnum.presentation.ui.theme.AccentColor

@Composable
fun ContactItem(contact: Contact,
        onRemove: () -> Unit,
        onFitContact: () -> Unit,
        toggleSelectionMode: Boolean = false,
                select: Boolean = false,
       onSelect:(value: Boolean)-> Unit = {}
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when(it) {
                SwipeToDismissBoxValue.EndToStart -> {
                    onFitContact()
                    false
                }
                SwipeToDismissBoxValue.StartToEnd -> {
                    onRemove()
                    false
                }
                else -> false
            }
        },
      //  positionalThreshold = { it * .25f }
    )


    SwipeToDismissBox(state = dismissState,
            enableDismissFromEndToStart = contact.hasPhoneNumberIssue()
        ,backgroundContent = {
        val color = when (dismissState.dismissDirection) {
            SwipeToDismissBoxValue.StartToEnd -> Color(0xFFFFA500)
            SwipeToDismissBoxValue.EndToStart -> AccentColor

            else -> Color.Transparent
        }

            val alignment = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Alignment.CenterStart
                SwipeToDismissBoxValue.EndToStart-> Alignment.CenterEnd
                else -> Alignment.Center
            }

            val icon = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.StartToEnd -> Icons.Default.Delete
                SwipeToDismissBoxValue.EndToStart -> Icons.Default.Edit
                else -> null
            }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color)

                .padding(horizontal = 20.dp)


            ,
            contentAlignment = alignment
        ) {

            if (dismissState.dismissDirection == SwipeToDismissBoxValue.StartToEnd) {
                icon?.let {
                    Icon(
                        painterResource(id = R.drawable.archivebox_fill),
                        contentDescription = "",
                        tint = Color.White
                    )
                }
            } else {
                Text(text = "Corriger", style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                ))
            }


        }
    } ) {

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val phoneNumbers = contact.phoneNumbers.joinToString {
                it.number
            }
            val displayData =
                if (contact.firstName.isNotEmpty() && contact.lastName.isNotEmpty()) {
                    "${contact.firstName[0]}${contact.lastName[0]}"
                } else if (contact.displayName.isNotEmpty()) {
                    contact.displayName[0].toString()
                } else {
                    "Ic"
                }

            if (contact.hasPhoneNumberIssue()) {
                Image(
                    painter = painterResource(id = R.drawable.person_crop_circle_badge_exclamationmark),
                    contentDescription = "Incorrect contact",

                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp), contentScale = ContentScale.Fit,
                    colorFilter = ColorFilter.tint(Color.Red)

                )
            } else {
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(50.dp)
                        .background(shape = CircleShape, color = Color.Gray.copy(alpha = 0.4f))

                ) {
                    Text(
                        text = displayData, style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = Color.Black
                        ), modifier = Modifier.align(Alignment.Center)
                    )
                }
            }



            Spacer(modifier = Modifier.width(10.dp))
            Column(horizontalAlignment = Alignment.Start) {

                if (contact.firstName.isEmpty() || contact.lastName.isEmpty()) {
                    Text(
                        text = contact.displayName, style = TextStyle(
                            fontWeight = FontWeight.W400
                        )
                    )
                } else {
                    Row {
                        Text(
                            text = contact.firstName, style = TextStyle(
                                fontWeight = FontWeight.W400
                            )
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = contact.lastName, style = TextStyle(
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        )

                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = phoneNumbers, style = TextStyle(
                        lineHeight = 20.sp
                    ), modifier = Modifier.padding(end = 10.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (toggleSelectionMode) {
                Checkbox(checked = select, onCheckedChange = {
                    onSelect(it)
                }, colors = CheckboxDefaults.colors(
                    checkedColor = AccentColor
                ), )
            }


        }



    }


}

















@Preview
@Composable
fun PrevContactItem() {
    val contact = Contact(1L,"Dupont","Dupont", "Dupont Dupont", listOf(
        PhoneNumber("+22861616161",1,"label"),
        PhoneNumber("+22861616162",1,"label"),
        PhoneNumber("+22861616163",1,"label"),
    ))
    ContactItem(contact, onRemove = {

    }, onFitContact = {

    })
}