package com.example.doubledegreeapp

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.doubledegreeapp.viewmodel.UserViewModel

import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavHostController, userViewModel: UserViewModel) {
    val scrollState = rememberScrollState()
    val userState by userViewModel.user.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var showLogoutDialog by remember { mutableStateOf(false) }

    Surface(
        color = Color("#EDEAF0".toColorInt()),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp)
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
                        .align(Alignment.CenterStart)
                ) {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.padding(bottom = 98.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black,
                        )
                    }
                }

                userState?.let { user ->
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user.profileImage)
                                    .placeholder(R.drawable.default_user)
                                    .error(R.drawable.default_user)
                                    .build()
                            ),
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            userState?.let { user ->
                Text(
                    text = user.name,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color("#6750A4".toColorInt())
                    ),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { navController.navigate("EditProfileScreen") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color("#6750A4".toColorInt()),
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
                        Text(text = "Edit Profile")
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { showLogoutDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color("#7c7c7c".toColorInt()),
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
                        Text(text = "Logout")
                    }
                }
            }
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text(text = "Confirm Logout") },
                text = { Text(text = "Are you sure you want to logout?") },
                confirmButton = {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                userViewModel.logout()
                                navController.navigate("LoginScreen")
                            }
                        }
                    ) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showLogoutDialog = false }
                    ) {
                        Text("No")
                    }
                }
            )
        }
    }
}