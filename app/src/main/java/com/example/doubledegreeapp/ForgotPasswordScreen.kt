package com.example.doubledegreeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()

    var isEmailValid by remember { mutableStateOf(true) }

    Surface(
        color = Color("#EDEAF0".toColorInt()),
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Please enter your email to send the code!",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Image(
                painter = painterResource(id = R.drawable.baseline_outgoing_mail_24),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                EmailInputField(
                    email = email,
                    onEmailChange = { email = it },
                    isValid = { isEmailValid = it },
                    onImeAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { /* Handle Sign In click */ },
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        top = 12.dp,
                        end = 12.dp,
                        bottom = 12.dp,
                    ),
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = 16.dp)
                        .width(227.dp)
                ) {
                    Text(text = "Send code")
                }
            }
        }
    }
}