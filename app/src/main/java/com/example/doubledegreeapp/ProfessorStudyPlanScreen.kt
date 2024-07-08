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
import androidx.navigation.NavHostController
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.doubledegreeapp.models.UserModel
import com.example.doubledegreeapp.viewmodel.StudyPlanViewModel
import com.example.doubledegreeapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import androidx.compose.material3.TextField

@Composable
fun ProfessorStudyPlanScreen(navController: NavHostController, userViewModel: UserViewModel, studyPlanViewModel: StudyPlanViewModel) {
    LaunchedEffect(Unit) {
        userViewModel.fetchUsersWithStudyPlan {  }
        userViewModel.fetchUsersWithoutStudyPlan {  }
    }

    val students by userViewModel.studentsWithStudyPlan.collectAsState(initial = emptyList())
    val studentsWithoutStudyPlan by userViewModel.studentsWithoutStudyPlan.collectAsState(initial = emptyList())

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
                    text = "Study Plan",
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
                        text = "Students who made their study plan",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray,
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(students.filter { !it.studyPlanReviewed }) { student ->
                    QualifiedStudentItemStudyPlan(student = student, userViewModel, studyPlanViewModel) { }
                }

                item {
                    Text(
                        text = "Students who did not make their study plan",
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.DarkGray
                        ),
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }

                items(studentsWithoutStudyPlan.filter { !it.prof }) { student ->
                    UnqualifiedStudentItem(student = student) { /* Handle item click */ }
                }
            }
        }
    }
}

@Composable
fun QualifiedStudentItemStudyPlan(
    student: UserModel,
    userViewModel: UserViewModel,
    studyPlanViewModel: StudyPlanViewModel,
    onClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var showDialog by remember { mutableStateOf(false) }
    var rejectionReason by remember { mutableStateOf("") }

    fun handleDownload(user: UserModel?) {
        coroutineScope.launch {
            studyPlanViewModel.downloadStudyPlan(context, url = user!!.studyPlan)
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
                            userViewModel.updateStudyPlanObservation(student.id, rejectionReason)
                            userViewModel.updateStudyPlanReviewed(student.id, true)
                            userViewModel.updateStudyPlanAccepted(student.id, false)
                            userViewModel.clearStudyPlan(student.id)
                            userViewModel.fetchUsersWithStudyPlan {}
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
                        modifier = Modifier.size(18.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Botão para aceitar o plano de estudos
            Surface(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .clickable {
                        coroutineScope.launch {
                            userViewModel.updateStudyPlanReviewed(student.id, true)
                            userViewModel.updateStudyPlanAccepted(student.id, true)
                            userViewModel.fetchUsersWithStudyPlan {}
                        }
                    },
                color = Color.Green,
            ) {
                Box(contentAlignment = Alignment.Center) {
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
fun UnqualifiedStudentItem(student: UserModel, onClick: () -> Unit) {
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