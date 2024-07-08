package com.example.doubledegreeapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.navigation.NavHostController
import com.example.doubledegreeapp.viewmodel.UserViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import androidx.activity.compose.BackHandler


@Composable
fun HomeScreen(navController: NavHostController, userViewModel: UserViewModel) {
    val scrollState = rememberScrollState()
    val user by userViewModel.user.collectAsState()
    val primaryColor = MaterialTheme.colorScheme.primary
    var tasksDones: Float = 0f

    BackHandler {
        // Vazio: não faz nada quando o botão de voltar é pressionado
    }

    Surface(
        color = Color("#EDEAF0".toColorInt()),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(16.dp)
                .verticalScroll(scrollState),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    user?.let { user ->
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(user.profileImage)
                                    .placeholder(R.drawable.default_user)
                                    .error(R.drawable.default_user)
                                    .build()
                            ),
                            contentDescription = "Profile Picture",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .clickable {
                                    navController.navigate("ProfileScreen")
                                }
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Welcome",
                            style = TextStyle(
                                color = Color(0xFF6750A4),
                                fontSize = 14.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal
                            )
                        )
                        Text(
                            text = user!!.name,
                            style = TextStyle(
                                color = Color(0xFF6750A4),
                                fontSize = 16.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { navController.navigate("QualifiedStudentsScreen") },
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Transparent, shape = RoundedCornerShape(4.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(4.dp))
                            .background(primaryColor, shape = RoundedCornerShape(4.dp))
                    ) {

                        Text(
                            text = "Students DMs",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { navController.navigate("ProfessorsChatsScreen") },
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Transparent, shape = RoundedCornerShape(4.dp)),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(4.dp))
                            .background(primaryColor, shape = RoundedCornerShape(4.dp))
                    ) {

                        Text(
                            text = "Professors DMs",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            MaterialTheme {
                if (user!!.studyPlanAccepted)
                    tasksDones+=1
                if (user!!.curriculumAccepted)
                    tasksDones+=1
                if (user!!.passportAccepted)
                    tasksDones+=1
                //Passar as tarefas feitas divido pelo total de tarefas no lugar desse 3f
                ProgressBox(taskdone = tasksDones, total = 3f)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Next Appointments",
                    style = TextStyle(
                        color = Color(0xFF6750A4),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                AppointmentItem(navController= navController,title="Study plan", description="Select the classes you wish to take", destinationRoute="StudyPlanScreen", date="18/03/2024", accepted = user!!.studyPlanAccepted, reviewed = user!!.studyPlanReviewed)
                AppointmentItem(navController= navController,title="Curriculum Vitae", description="You need to create your IPB curriculum", destinationRoute="CurriculumVitaeScreen", date="20/03/2024", accepted = user!!.curriculumAccepted, reviewed = user!!.curriculumReviewed)
                AppointmentItem(navController= navController,title="Passport", description="You need to submit your passport", destinationRoute="PassportScreen", date="22/03/2024", accepted = user!!.passportAccepted, reviewed = user!!.passportReviewed)
                AppointmentItem(navController= navController,title="Download Letter of acceptance", description="Available after completed all steps", destinationRoute="LetterOfAcceptanceScreen", date="30/03/2024", isDownload = true)
            }
        }
    }
}