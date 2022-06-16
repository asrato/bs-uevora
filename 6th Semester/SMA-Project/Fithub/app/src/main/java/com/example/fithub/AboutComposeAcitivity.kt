package com.example.fithub

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fithub.ui.theme.FithubTheme
import java.util.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

class AboutComposeAcitivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DefaultPreview()
        }
    }
}


@Composable
fun TopBar() {
    val theme = if (isSystemInDarkTheme()) Color(0xFF000000) else Color(0xFFCF7623)
    TopAppBar(
        title = { Text(text = "Fithub") },
        contentColor = Color.White,
        backgroundColor = theme
    )
}


@Composable

fun Logo() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painterResource(R.drawable.fithub),
            contentDescription = "",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .clip(shape = RectangleShape)
                .size(100.dp)
        )
    }
}

@Composable
fun About_text() {
    val theme = if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF000000)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text( text = stringResource(R.string.About_1), fontWeight = FontWeight.Bold, color = theme, textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.About_2), fontWeight = FontWeight.Bold, color = theme, textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.About_3), fontWeight = FontWeight.Bold, color = theme, textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.About_4), fontWeight = FontWeight.Bold, color = theme, textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.About_5), fontWeight = FontWeight.Bold, color = theme, textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.About_6), fontWeight = FontWeight.Bold, color = theme, textAlign = TextAlign.Center)
        Text(text = stringResource(R.string.About_7), fontWeight = FontWeight.Bold, color = theme, textAlign = TextAlign.Center)
    }
}


@Composable
fun Names() {
    val theme = if (isSystemInDarkTheme()) Color(0xFFFFFFFF) else Color(0xFF000000)
    Row() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = stringResource(R.string.made_by),
                textAlign = TextAlign.Center,
                color = theme
            )
            Text("l44448 - Ricardo Matono", textAlign = TextAlign.Center, color = theme)
            Text("l44451 - João Rouxinol", textAlign = TextAlign.Center, color = theme)
            Text("l45517 - André Rato", textAlign = TextAlign.Center, color = theme)
        }
    }
}


@Composable
fun Back_button() {
    val theme = if (isSystemInDarkTheme()) Color(0xFF000000) else Color(0xFFFFFFFF)
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End) {
        Button(
            onClick = { context.startActivity(Intent(context, AppMenu::class.java)) },
            shape = RoundedCornerShape(15),
            colors = ButtonDefaults.outlinedButtonColors(
                backgroundColor = Color(0xFFCF7623),
                contentColor = theme
            )
        ) {
            Text(text = stringResource(R.string.back))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FithubTheme {
        Column {
            TopBar()
            Back_button()
            Logo()
            Column(
                Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                About_text()
                Names()
            }
        }
    }
}