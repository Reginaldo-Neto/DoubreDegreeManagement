package com.example.doubledegreeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Surface
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun ProfessorProfileScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()

    Surface(
        color = Color("#EDEAF0".toColorInt()),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                ) {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black,
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mia),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Text(
                text = "Maria Joaquina",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color("#6750A4".toColorInt())
                ),
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.outline_place_24),
                    contentDescription = "Upload",
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(2.dp))

                Text(
                    text = "Brazil",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color("#79747E".toColorInt())
                    ),
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = { /* navigate to chat */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF79747E),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        top = 12.dp,
                        end = 12.dp,
                        bottom = 12.dp,
                    ),
                    modifier = Modifier
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                        .widthIn(min = 140.dp, max = 400.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.outline_message_24),
                            contentDescription = "Upload",
                            modifier = Modifier.size(18.dp)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Text(text = "Send Message")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Handle steps button click */ },
                modifier = Modifier
                    .height(72.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .border(2.dp, Color.White, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text(
                                text = "33",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(start = 1.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = "1/3 steps concluded")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* Handle Study Plan button click */ },
                modifier = Modifier
                    .height(72.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Icon(
                        painter = painterResource(R.drawable.outline_file_download_24),
                        contentDescription = "Upload",
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = "Study Plan")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* Handle CV button click */ },
                modifier = Modifier
                    .height(72.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF79747E),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Icon(
                        painter = painterResource(R.drawable.outline_file_download_24),
                        contentDescription = "Upload",
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = "CV")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* Handle Complete Application button click */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF79747E),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .height(72.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Icon(
                        painter = painterResource(R.drawable.outline_file_download_24),
                        contentDescription = "Upload",
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = "Complete Application")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfessorProfileScreenPreview() {
    val navController = rememberNavController()
    ProfessorProfileScreen(navController)
}