package com.example.doubledegreeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun WelcomeScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.welcome_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                alpha = 0.6F,
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Welcome to\n DD Process!",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                )

                Spacer(modifier = Modifier.weight(1f))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(                        

                            onClick =  { navController.navigate("LoginScreen") },

                            contentPadding = PaddingValues(
                                start = 12.dp,
                                top = 12.dp,
                                end = 12.dp,
                                bottom = 12.dp,
                            ),
                            modifier = Modifier
                                .wrapContentWidth(align = Alignment.CenterHorizontally)
                                .widthIn(min = 250.dp, max = 400.dp)
                        ) {
                            Text(
                                color = Color.White,
                                text = "Sign In"
                            )
                        }
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    val navController = rememberNavController()
    WelcomeScreen(navController)
}