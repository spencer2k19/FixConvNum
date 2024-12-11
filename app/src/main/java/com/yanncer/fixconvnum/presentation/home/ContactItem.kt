package com.yanncer.fixconvnum.presentation.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun ContactItem(contact: Contact) {


    Row(modifier = Modifier
        .fillMaxWidth()

        .padding(bottom = 20.dp)) {



       val phoneNumbers = contact.phoneNumbers.joinToString {
           it.number
       }

        


        val displayData = if(contact.firstName.isNotEmpty() && contact.lastName.isNotEmpty()) {
            "${contact.firstName[0]}${contact.lastName[0]}"
        } else if (contact.displayName.isNotEmpty()) {
            contact.displayName[0].toString()
        } else {
            "Ic"
        }

        if(contact.hasPhoneNumberIssue()) {
            Image(painter = painterResource(id = R.drawable.person_crop_circle_badge_exclamationmark),
                contentDescription = "Incorrect contact",

                modifier  = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    , contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color.Red)

            )
        } else {
            Box(modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .background(shape = CircleShape, color = Color.Gray.copy(alpha = 0.4f))

            ) {
                Text(text = displayData, style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Color.Black
                ), modifier = Modifier.align(Alignment.Center))
            }
        }



        Spacer(modifier = Modifier.width(10.dp))
        Column(horizontalAlignment = Alignment.Start) {
            if(contact.firstName.isEmpty() || contact.lastName.isEmpty()) {
                Text(text = contact.displayName, style = TextStyle(
                    fontWeight = FontWeight.W400
                ))
            } else {
                Row {
                    Text(text = contact.firstName, style = TextStyle(
                        fontWeight = FontWeight.W400
                    )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = contact.lastName, style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    ))

                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = phoneNumbers, style = TextStyle(
                lineHeight = 20.sp
            ), modifier = Modifier.padding(end = 10.dp)
            )
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
    ContactItem(contact)
}