package com.example.doubledegreeapp

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.navigation.NavHostController
import com.example.doubledegreeapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(navController: NavHostController, userViewModel: UserViewModel) {
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var degree by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isNameValid by remember { mutableStateOf(true) }
    var degreeIsValid by remember { mutableStateOf(true) }
    var passwordIsValid by remember { mutableStateOf(true) }
    var confirmPasswordIsValid by remember { mutableStateOf(true) }
    var phoneNumberIsValid by remember { mutableStateOf(true) }

    val focusManager = LocalFocusManager.current

    val scrollState = rememberScrollState()

    val coroutineScope = rememberCoroutineScope()

    val loading by userViewModel.loadingRegister.collectAsState()
    val error by userViewModel.errorRegister.collectAsState()

    fun handleSignUp() {
        coroutineScope.launch {
            userViewModel.register(
                name = name,
                phone = phoneNumber,
                degree = degree,
                password = password,
                onSuccess = {
                    navController.navigate("HomeScreen")
                }
            );
        }
    }

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
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Create a new account!!",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color("#6750A4".toColorInt())
                ),
            )
            Text(
                text = "Let’s get start",
                textAlign = TextAlign.Center,
                style = TextStyle(
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color("#6750A4".toColorInt())
                ),
            )

            Spacer(modifier = Modifier.height(24.dp))

            NameInputField(
                name = name,
                onNameChange = { name = it },
                isValid = { isNameValid = it },
                onImeAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PhoneNumberInputField(
                phoneNumber = phoneNumber,
                onPhoneNumberChange = { phoneNumber = it },
                isValid = { phoneNumberIsValid = it },
                onImeAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            DegreeInputField(
                degree = degree,
                onDegreeChange = { degree = it },
                isValid = { degreeIsValid = it },
                onImeAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )



            Spacer(modifier = Modifier.height(16.dp))

            PasswordInputField(
                password = password,
                onPasswordChange = { password = it },
                isValid = { passwordIsValid = it },
                onImeAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ConfirmPasswordInputField(
                passwordOriginal = password,
                password = confirmPassword,
                onPasswordChange = { confirmPassword = it },
                isValid = { confirmPasswordIsValid = it },
                onImeAction = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            )

            Button(
                onClick = { handleSignUp() },
                enabled = !loading,
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
                if (loading) {
                    CircularProgressIndicator(modifier = Modifier.size(22.dp), color = Color.White)
                } else {
                    Text(text = "Sign Up")
                }
            }

            if(error !== "") {
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }
    }
}