package com.example.doubledegreeapp

import android.util.Log
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
import androidx.compose.material3.TextField
import com.example.doubledegreeapp.models.UserModel
import com.example.doubledegreeapp.viewmodel.UserViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext

@Composable
fun QualifiedStudentsScreen(navController: NavHostController, userViewModel: UserViewModel) {
    LaunchedEffect(Unit) {
        userViewModel.fetchStudents { }
    }

    val students by userViewModel.allStudents.collectAsState(initial = emptyList())
    val user by userViewModel.user.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
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
                    text = "Students Chat",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color("#6750A4".toColorInt())
                    ),
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search Students") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            val filteredStudents = students.filter {
                it.id != user!!.id && it.name.contains(searchQuery, ignoreCase = true)
            }
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredStudents) { student ->
                    Log.d("StudentProfileImage", "Profile Image URL: ${student.profileImage}")
                    QualifiedStudentItem(student) {}
                }
            }
        }
    }
}

@Composable
fun QualifiedStudentItem(student: UserModel, onClick: () -> Unit) {

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
                    )
                )

            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
                contentDescription = "Arrow Forward",
                tint = Color.Gray
            )
        }
    }
}