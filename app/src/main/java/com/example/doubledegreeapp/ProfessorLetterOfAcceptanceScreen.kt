package com.example.doubledegreeapp

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.doubledegreeapp.models.UserModel
import com.example.doubledegreeapp.viewmodel.LetterOfAcceptanceViewModel
import com.example.doubledegreeapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun ProfessorLetterOfAcceptanceScreen(navController: NavHostController, userViewModel: UserViewModel, letterOfAcceptanceViewModel: LetterOfAcceptanceViewModel) {
    LaunchedEffect(Unit) {
        userViewModel.fetchUsersWithDocuments { }
    }

    val students by userViewModel.studentsWithDocuments.collectAsState(initial = emptyList())

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
                    text = "Letter of acceptance",
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
                items(students) { student ->
                    val icon = if (student.letterOfAcceptance == "") {
                        painterResource(id = R.drawable.outline_file_upload_24)
                    } else {
                        painterResource(id = R.drawable.baseline_close_24)
                    }
                    QualifiedStudentItem(student = student, letterOfAcceptanceViewModel = letterOfAcceptanceViewModel, userViewModel = userViewModel, icon = icon)
                }
            }
        }
    }
}

@Composable
fun QualifiedStudentItem(
    student: UserModel,
    letterOfAcceptanceViewModel: LetterOfAcceptanceViewModel,
    userViewModel: UserViewModel,
    icon: Painter
) {
    val coroutineScope = rememberCoroutineScope()

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
    }

    fun handleSave(userId: String) {
        if (selectedFileUri != null) {
            coroutineScope.launch {
                try {
                    letterOfAcceptanceViewModel.uploadLetterOfAcceptance(
                        uri = selectedFileUri!!,
                        onSuccess = {
                            coroutineScope.launch {
                                userViewModel.fetchUsersWithDocuments { }
                            }
                        },
                        userId = userId
                    )
                } catch (e: Exception) {
                    Log.e("QualifiedStudentItem", "Error uploading letter of acceptance", e)
                }
            }
        }
    }

    LaunchedEffect(selectedFileUri) {
        if (selectedFileUri != null) {
            handleSave(student.id)
        }
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Lida com o clique no item, se necessário */ },
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

            if (student.letterOfAcceptance != "") {
                Surface(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .clickable {
                            coroutineScope.launch {
                                userViewModel.clearLetterOfAcceptance(student.id)
                                userViewModel.fetchUsersWithDocuments { }
                            }
                        },
                    color = Color("#6750A4".toColorInt()),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_close_24),
                            contentDescription = "Clear",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            } else {
                Surface(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .clickable {
                            launcher.launch("application/pdf")
                        },
                    color = Color("#6750A4".toColorInt()),
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_file_upload_24),
                            contentDescription = "Upload",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}