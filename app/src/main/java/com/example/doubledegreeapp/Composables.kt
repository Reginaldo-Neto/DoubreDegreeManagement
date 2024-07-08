package com.example.doubledegreeapp

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun NameInputField(name: String, onNameChange: (String) -> Unit, onImeAction: () -> Unit, isValid: (Boolean) -> Unit) {
    var isTouched by remember { mutableStateOf(false) }
    val namePattern = Regex("^[a-zA-ZáéíóúÁÉÍÓÚãõÃÕâêîôûÂÊÎÔÛçÇ ]+$")
    val isNameValid = name.trim().split(" ").size > 1 && name.matches(namePattern)

    isValid(isNameValid)

    OutlinedTextField(
        value = name,
        onValueChange = {
            onNameChange(it)
            isTouched = true
        },
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                if (isNameValid) {
                    onImeAction()
                }
            }
        ),
        singleLine = true,
        isError = isTouched && !isNameValid && name.trim().length<6,
        textStyle = TextStyle(color = Color.Black)
    )
    if(isTouched && !name.matches(namePattern)){
        Text(
            text = "Please enter a valid name.",
            color = Color.Red,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }else if(isTouched && name.trim().length<6 ){
        Text(
            text = "Please enter a valid name.",
            color = Color.Red,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }else if (isTouched && !isNameValid) {
        Text(
            text = "Please enter both first and last name.",
            color = Color.Red,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}

@Composable
fun EmailInputField(email: String, onEmailChange: (String) -> Unit, onImeAction: () -> Unit, isValid: (Boolean) -> Unit) {
    var isTouched by remember { mutableStateOf(false) }
    val emailPattern = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    val isEmailValid = email.matches(emailPattern)

    isValid(isEmailValid)

    OutlinedTextField(
        value = email,
        onValueChange = {
            onEmailChange(it)
            isTouched = true
        },
        label =  { Text("Email") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                if (isEmailValid) {
                    onImeAction()
                }
            }
        ),
        singleLine = true,
        isError = isTouched && !isEmailValid,
        textStyle = TextStyle(color = Color.Black)
    )

    if (isTouched && !isEmailValid) {
        Text(
            text = "Please enter a valid email address.",
            color = Color.Red,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}

@Composable
fun PhoneNumberInputField(phoneNumber: String, onPhoneNumberChange: (String) -> Unit, onImeAction: () -> Unit, isValid: (Boolean) -> Unit) {
    var isTouched by remember { mutableStateOf(false) }
    val isPhoneNumberValid =
        phoneNumber.length >= 10 && phoneNumber.all { it.isDigit() }

    isValid(isPhoneNumberValid)

    OutlinedTextField(
        value = phoneNumber,
        onValueChange = {
            onPhoneNumberChange(it)
            isTouched = true
        },
        label = { Text("Phone Number") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                if (isPhoneNumberValid) {
                    onImeAction()
                }
            }
        ),
        singleLine = true,
        isError = isTouched && !isPhoneNumberValid,
        textStyle = TextStyle(color = Color.Black)
    )

    if (isTouched && !isPhoneNumberValid) {
        Text(
            text = "Please enter a valid phone number with at least 10 digits.",
            color = Color.Red,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}

@Composable
fun DegreeInputField(degree: String, onDegreeChange: (String) -> Unit, onImeAction: () -> Unit, isValid: (Boolean) -> Unit) {
    var isTouched by remember { mutableStateOf(false) }
    val degreePattern = Regex("^[a-zA-ZáéíóúÁÉÍÓÚãõÃÕâêîôûÂÊÎÔÛçÇ ]+$")
    val isDegreeValid = degree.trim().split(" ").size > 1 && degree.matches(degreePattern)

    isValid(isDegreeValid)

    OutlinedTextField(
        value = degree,
        onValueChange = {
            onDegreeChange(it)
            isTouched = true
        },
        label = { Text("Degree/Program") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                if (isDegreeValid) {
                    onImeAction()
                }
            }
        ),
        singleLine = true,
        isError = isTouched && !isDegreeValid,
        textStyle = TextStyle(color = Color.Black)
    )

    if (isTouched && !isDegreeValid) {
        Text(
            text = "Please enter both first and last name of degree.",
            color = Color.Red,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}

@Composable
fun PasswordInputField(
    password: String,
    onPasswordChange: (String) -> Unit,
    onImeAction: () -> Unit,
    isValid: (Boolean) -> Unit
) {
    var isTouched by remember { mutableStateOf(false) }

    val passwordPattern = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#\$%^&*]).{8,}$")
    val isPasswordValid = password.matches(passwordPattern)

    isValid(isPasswordValid)

    OutlinedTextField(
        value = password,
        onValueChange = {
            onPasswordChange(it)
            isTouched = true
        },
        label = { Text("Password") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                if (isPasswordValid) {
                    onImeAction()
                }
            }
        ),
        singleLine = true,
        isError = isTouched && !isPasswordValid,
        textStyle = TextStyle(color = Color.Black)
    )

    if (isTouched && !isPasswordValid) {
        Text(
            text = "Password must be at least 8 characters, one uppercase letter, one number, and one special character.",
            color = Color.Red,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
        )
    }
}

@Composable
fun ConfirmPasswordInputField(
    passwordOriginal: String,
    password: String,
    onPasswordChange: (String) -> Unit,
    onImeAction: () -> Unit,
    isValid: (Boolean) -> Unit
) {
    var isTouched by remember { mutableStateOf(false) }

    val passwordPattern = Regex("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#\$%^&*]).{8,}$")
    val isPasswordValid = password.matches(passwordPattern)

    isValid(isPasswordValid)

    OutlinedTextField(
        value = password,
        onValueChange = {
            onPasswordChange(it)
            isTouched = true
        },
        label = { Text("Password") },
        modifier = Modifier.fillMaxWidth(),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = {
                if (isPasswordValid) {
                    onImeAction()
                }
            }
        ),
        singleLine = true,
        isError = isTouched && !isPasswordValid,
        textStyle = TextStyle(color = Color.Black)
    )
    if (isTouched && !isPasswordValid) {
        Text(
            text = "Password must be at least 8 characters, one uppercase letter, one number, and one special character.",
            color = Color.Red,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
        )
    }else if(isTouched && (passwordOriginal != password)){
        Text(
            text = "Passwords do not match.",
            color = Color.Red,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp),
        )
    }
}


@Composable
fun AppointmentItem(
    navController: NavController,
    title: String,
    description: String,
    destinationRoute: String,
    date: String,
    isDownload: Boolean = false,
    accepted: Boolean = false,
    reviewed: Boolean = false
) {
    val monthMap = mapOf(
        "01" to "JAN", "02" to "FEB", "03" to "MAR",
        "04" to "APR", "05" to "MAY", "06" to "JUN",
        "07" to "JUL", "08" to "AUG", "09" to "SEP",
        "10" to "OCT", "11" to "NOV", "12" to "DEC"
    )

    val day = date.substring(0, 2)
    val month = date.substring(3, 5)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .clickable { navController.navigate(destinationRoute) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(Color(0xFFD9D9D9), shape = RoundedCornerShape(4.dp))
                .padding(4.dp),
            contentAlignment = Alignment.Center,

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if(isDownload){
                    Icon(
                        painter = painterResource(R.drawable.outline_file_download_24),
                        contentDescription = "Upload",
                        modifier = Modifier.size(40.dp)
                    )
                }else{
                Text(
                    text = monthMap[month] ?: "",
                    style = TextStyle(
                        color = Color.Red,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = day,
                    style = TextStyle(
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )}
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = TextStyle(
                    color = Color(0xFF6750A4),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = description,
                style = TextStyle(
                    color = Color(0xFF6750A4),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if(!accepted && !reviewed){
            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
                contentDescription = "More details",
                tint = Color(0xFF6750A4),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }else if(!accepted && reviewed){
            Icon(
                painter = painterResource(id = R.drawable.baseline_warning_24),
                contentDescription = "Warning",
                tint = Color(0xffe4ac00),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }else if(accepted && reviewed){
            Icon(
                painter = painterResource(id = R.drawable.baseline_check_24),
                contentDescription = "Correct",
                tint = Color(0xFF008000),
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}@Composable
fun ProgressCircle(taskdone:Float, total:Float) {
    val percentage = (100/total)*taskdone
    val sweepAngle = (percentage / 100) * 360
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(CircleShape)
            .aspectRatio(1f)
            .background(primaryColor),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(40.dp)) {
            drawArc(
                color = Color.White,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Text(
            text = "${percentage.toInt()}%",
            style = TextStyle(
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Composable
fun ProgressBox(taskdone:Float, total:Float) {

    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(primaryColor, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            ProgressCircle(taskdone, total)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "${taskdone.toInt()}/${total.toInt()} steps concluded",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}