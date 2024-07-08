package com.example.doubledegreeapp

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.compose.material3.Surface
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavHostController
import com.example.doubledegreeapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController, userViewModel: UserViewModel) {
    val coroutineScope = rememberCoroutineScope()

    val loading by userViewModel.loadingLogin.collectAsState()
    val error by userViewModel.errorLogin.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    fun handleSignIn() {
        coroutineScope.launch {
            userViewModel.login(
                email = email,
                password = password,
                onSuccess = { user ->
                    if(user != null) {
                        if(user.prof) {
                            navController.navigate("ProfessorsHomeScreen")
                        } else {
                            navController.navigate("HomeScreen")
                        }
                    } else {
                        navController.navigate("RegisterScreen")
                    }
                }
            );
        }
    }

    Surface(
        color = Color("#EDEAF0".toColorInt()),
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hello!",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color("#6750A4".toColorInt())
                ),
            )
            Text(
                text = "Make your Login",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color("#6750A4".toColorInt())
                ),
            )

            Spacer(modifier = Modifier.height(24.dp))

            EmailInputField(
                email = email,
                onEmailChange = { email = it },
                isValid = { isEmailValid = it },
                onImeAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )

            PasswordInputField(
                password = password,
                onPasswordChange = { password = it },
                isValid = { isPasswordValid = it },
                onImeAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )



            Button(
                enabled = isEmailValid && isPasswordValid,
                onClick = { handleSignIn() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6750A4),
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(
                    start = 12.dp,
                    top = 12.dp,
                    end = 12.dp,
                    bottom = 12.dp,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White)
                } else {
                    Text(text = "Sign In")
                }
            }

            if(error !== "") {
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = { navController.navigate("ForgotPasswordScreen") })
                {
                    Text(
                        text = "Forgot my password",
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            color = Color("#6750A4".toColorInt())
                        ),
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    style = TextStyle(
                        fontWeight = FontWeight.Medium,
                        color = Color("#79747E".toColorInt())
                    ),
                )
                TextButton(onClick = { navController.navigate("RegisterScreen") }) {
                    Text(
                        text = "Register",
                        style = TextStyle(
                            fontWeight = FontWeight.Medium,
                            color = Color("#6750A4".toColorInt())
                        ),
                    )
                }
            }
        }
    }
}
