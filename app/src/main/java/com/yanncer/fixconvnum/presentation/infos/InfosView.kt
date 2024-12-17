package com.yanncer.fixconvnum.presentation.infos

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.yanncer.fixconvnum.R
import com.yanncer.fixconvnum.presentation.ui.theme.AccentColor
import com.yanncer.fixconvnum.presentation.ui.theme.Typography


@Composable
fun InfosView() {

    val context = LocalContext.current
    val appTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    val opacity = if (isSystemInDarkTheme()) 0.1f else 0.07f



    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        //.background(if (isSystemInDarkTheme()) Color.Black else Color.White)
        .padding(horizontal = 20.dp, vertical = 20.dp)

    ) {
        Text(text = "Comment utiliser FixConvNum ?", style = Typography.titleMedium.copy(
            fontSize = 18.sp
        ), textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally))
        Spacer(modifier = Modifier.height(20.dp))

            Row(modifier = Modifier
                .fillMaxWidth()

                .border(
                    width = 1.dp,
                    color = appTextColor.copy(opacity),
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically


            ) {
                Box(modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .background(
                        shape = CircleShape,
                        color = appTextColor.copy(alpha = if (isSystemInDarkTheme())0.08f else 0.04f)
                    )
                    .border(width = 1.dp, color = appTextColor.copy(alpha = 0.1f), shape = CircleShape)

                ) {
                    Text(text = "SH", style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,

                    ), modifier = Modifier.align(Alignment.Center))
                }

                Spacer(modifier = Modifier.width(15.dp))
                Column(horizontalAlignment = Alignment.Start) {
                    Row {
                        Text(text = "Steve", style = TextStyle(
                            fontWeight = FontWeight.W400,
                            fontSize = 17.sp
                        )
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "Jobs", style = TextStyle(

                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        )

                    }
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = "+22900000001, 0100000001", style = TextStyle(
                        lineHeight = 20.sp,
                        fontSize = 15.sp
                    ), modifier = Modifier.padding(end = 10.dp)
                    )
                }
            }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = buildAnnotatedString {
            append("Les contacts affichés suivant le format ci-dessus ont tous leurs numéros")
            withStyle(
                SpanStyle(
                   color = AccentColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            ) {
                append(" béninois ")
            }
            append("déjà migrés et contiennent aussi le format non migré pour permettre aux applications sociales comme")
            withStyle(
                SpanStyle(
                    color = AccentColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            ) {
                append(" WhatsApp ")

            }
            append("de les identifier.")
        }, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(20.dp))
        Row(modifier = Modifier
            .fillMaxWidth()

            .border(
                width = 1.dp,
                color = appTextColor.copy(opacity),
                shape = RoundedCornerShape(10.dp)
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .width(80.dp)
                .height(80.dp)

                .background(
                    color = Color(0xFFFF9800), shape = RoundedCornerShape(
                        topStart = 10.dp,
                        bottomStart = 10.dp
                    )
                )

            ) {
                Image(painter = painterResource(id = R.drawable.archivebox_fill) ,
                    colorFilter = ColorFilter.tint(Color.White),
                    contentDescription = "archive data",
                    modifier = Modifier.align(Alignment.Center))
            }
            Spacer(modifier = Modifier.width(10.dp))
            Box(modifier = Modifier
                .width(50.dp)
                .height(50.dp)
                .background(
                    shape = CircleShape,
                    color = appTextColor.copy(alpha = if (isSystemInDarkTheme())0.08f else 0.04f)
                )
                .border(width = 1.dp, color = appTextColor.copy(alpha = 0.1f), shape = CircleShape)
                .padding(vertical = 10.dp)

            ) {
                Text(text = "SH", style = TextStyle(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,

                ), modifier = Modifier.align(Alignment.Center))
            }

            Spacer(modifier = Modifier.width(15.dp))
            Column(modifier = Modifier.padding(vertical = 10.dp),horizontalAlignment = Alignment.Start) {
                Row {
                    Text(text = "Steve", style = TextStyle(
                        fontWeight = FontWeight.W400,
                        fontSize = 17.sp
                    )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "Jobs", style = TextStyle(

                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    )

                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "+22900000001, 0100000001", style = TextStyle(
                    lineHeight = 20.sp,
                    fontSize = 15.sp
                ), modifier = Modifier.padding(end = 10.dp)
                )
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Vous pouvez ignorer certains contacts en les glissant vers la droite. Ces derniers seront retirés de l'application mais pas de votre répertoire téléphonique. Ils ne seront pas altérés."
        , style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier
            .fillMaxWidth()

            .border(
                width = 1.dp,
                color = appTextColor.copy(opacity),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically


        ) {
            Image(painter = painterResource(id = R.drawable.person_crop_circle_badge_exclamationmark),
                contentDescription = "Incorrect contact", colorFilter = ColorFilter.tint(Color.Red),
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp))

            Spacer(modifier = Modifier.width(15.dp))
            Column(horizontalAlignment = Alignment.Start) {
                Row {
                    Text(text = "Steve", style = TextStyle(
                        fontWeight = FontWeight.W400,
                        fontSize = 17.sp
                    )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "Jobs", style = TextStyle(

                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    )

                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "97000000", style = TextStyle(
                    lineHeight = 20.sp,
                    fontSize = 15.sp
                ), modifier = Modifier.padding(end = 10.dp)
                )

            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(text = buildAnnotatedString {
            append("Les contacts affichés suivant le format ci-dessus se retrouvent dans le cas contraire. C'est-à-dire qu'ils")
            withStyle(
                SpanStyle(
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            ) {
                append(" n'ont pas été migré ")
            }
            append(" ou que lors de la migration, ils ont été ")
            withStyle(
                SpanStyle(
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            ) {
                append(" remplacés ")

            }
            append(" par le nouveau format des numeros béninois. Ils sont à l'origine des problèmes rencontrés dans l'application")
            withStyle(
                SpanStyle(

                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            ) {
                append(" WhatsApp ")

            }
            append(" par exemple.")
        }, style = MaterialTheme.typography.bodyMedium.copy(
            lineHeight = 25.sp
        ))

        Spacer(modifier = Modifier.height(25.dp))

        Row(modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = appTextColor.copy(opacity),
                shape = RoundedCornerShape(10.dp)
            ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically

        ) {
            Column(horizontalAlignment = Alignment.Start,modifier = Modifier
                .padding(vertical = 10.dp)
              //  .background(color = Color.White)

            ) {
                Row {
                    Text(text = "Steve", style = TextStyle(
                        fontWeight = FontWeight.W400,
                        fontSize = 17.sp
                    )
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(text = "Jobs", style = TextStyle(

                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                    )

                }
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = "97000000", style = TextStyle(
                    lineHeight = 20.sp
                ), modifier = Modifier.padding(end = 10.dp)
                )
            }

            Box(modifier = Modifier
                .width(80.dp)
                .height(70.dp)
                .background(
                    color = AccentColor, shape = RoundedCornerShape(
                        topEnd = 10.dp,
                        bottomEnd = 10.dp
                    )
                )

            ) {
                Text(text = "Corriger", style = MaterialTheme.typography.bodyMedium.copy(color = Color.White,

                    fontWeight = FontWeight.Bold),

                    modifier = Modifier.align(Alignment.Center))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "Vous pouvez aussi corriger un contact à la fois, en le glissant vers la gauche.", style = MaterialTheme.typography
            .bodyMedium)
        Spacer(modifier = Modifier.height(40.dp))



        TextButton(onClick = {
            val httpIntent = Intent(Intent.ACTION_VIEW,Uri.parse("https://www.linkedin.com/in/loic-hacheme/"))
            context.startActivity(httpIntent)

        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "By", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = "@loic", style = MaterialTheme.typography.bodyMedium.copy(
                color = AccentColor,
                fontWeight = FontWeight.Normal
            ))
        }

        Spacer(modifier = Modifier.height(40.dp))







    }
}





@Preview(
   uiMode = Configuration.UI_MODE_NIGHT_NO,
    showBackground = true
)
@Composable
fun PrevInfosView() {
    InfosView()
}