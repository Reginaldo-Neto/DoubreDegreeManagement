package com.example.doubledegreeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.compose.material3.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.doubledegreeapp.models.UserModel
import com.example.doubledegreeapp.viewmodel.UserViewModel
import androidx.compose.runtime.*
import com.example.doubledegreeapp.viewmodel.CurriculumVitaeViewModel
import com.example.doubledegreeapp.viewmodel.PassportViewModel
import kotlinx.coroutines.launch
import androidx.compose.material3.TextField

@Composable
fun ProfessorPassportScreen(navController: NavHostController, userViewModel: UserViewModel, passportViewModel: PassportViewModel) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        userViewModel.fetchUsersWithPassport {  }
        userViewModel.fetchUsersWithoutPassport {  }
    }

    val students by userViewModel.studentsWithPassport.collectAsState(initial = emptyList())
    val studentsWithoutPassport by userViewModel.studentsWithoutPassport.collectAsState(initial = emptyList())

    Surface(
        color = Color("#EDEAF0".toColorInt()),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Passport",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color("#6750A4".toColorInt())
                    ),
                )
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Text(
                        text = "Students who uploaded their passport",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray,
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(students.filter { !it.passportReviewed }) { student ->
                    QualifiedStudentItemPassport(student = student, userViewModel, passportViewModel, onClick = {  })
                }

                item {
                    Text(
                        text = "Students who did not uploaded their passport",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray
                        ),
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                items(studentsWithoutPassport.filter { !it.prof }) { student ->
                    UnqualifiedItemPassport(student = student, onClick = { /* Handle item click */ })
                }
            }
        }
    }
}

@Composable
fun QualifiedStudentItemPassport(
    student: UserModel,
    userViewModel: UserViewModel,
    passportViewModel: PassportViewModel,
    onClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var rejectionReason by remember { mutableStateOf("") }

    fun handleDownload(user: UserModel?) {
        coroutineScope.launch {
            passportViewModel.downloadPassport(context, url = user!!.passport)
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Motivo da Recusa")
            },
            text = {
                Column {
                    Text("Por favor, forneça o motivo para a recusa do documento:")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = rejectionReason,
                        onValueChange = { rejectionReason = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        coroutineScope.launch {
                            userViewModel.updatePassportObservation(student.id, rejectionReason)
                            userViewModel.updatePassportReviewed(student.id, true)
                            userViewModel.updatePassportAccepted(student.id, false)
                            userViewModel.clearPassport(student.id)
                            userViewModel.fetchUsersWithPassport {}
                        }
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = Color.White,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(student.profileImage)
                        .placeholder(R.drawable.default_user)
                        .error(R.drawable.default_user)
                        .build()
                ),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = student.name,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color("#6750A4".toColorInt())
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = 130.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            Surface(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable {
                        handleDownload(student)
                    },
                color = Color("#6750A4".toColorInt()),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = R.drawable.outline_file_download_24),
                        contentDescription = "Icon",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Surface(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable {
                        showDialog = true
                    },
                color = Color.Red,
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_close_24),
                        contentDescription = "Icon",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Surface(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable {
                        coroutineScope.launch {
                            userViewModel.updatePassportReviewed(student.id, true)
                            userViewModel.updatePassportAccepted(student.id, true)
                            userViewModel.fetchUsersWithPassport {}
                        }
                    },
                color = Color.Green
            ) {
                Box(contentAlignment = Alignment.Center,) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_check_24),
                        contentDescription = "Icon",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun UnqualifiedItemPassport(student: UserModel, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        color = Color.White,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(student.profileImage)
                        .placeholder(R.drawable.default_user)
                        .error(R.drawable.default_user)
                        .build()
                ),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = student.name,
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color("#6750A4".toColorInt())
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = 200.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))


            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
                    contentDescription = "Icon",
                    tint = Color.LightGray,
                    modifier = Modifier.size(32.dp)
                )
            }

        }
    }
}