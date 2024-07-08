package com.example.doubledegreeapp

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Surface
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.doubledegreeapp.viewmodel.StudyPlanViewModel
import com.example.doubledegreeapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity

@Composable
fun StudyPlanScreen(navController: NavHostController, userViewModel: UserViewModel) {
    val studyPlanViewModel: StudyPlanViewModel = viewModel()
    val context = LocalContext.current
    val user by userViewModel.user.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    println(selectedFileUri?.path)

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
    }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                (context as Activity),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }

    fun handleSave() {
        coroutineScope.launch {
            studyPlanViewModel.uploadStudyPlan(
                uri = selectedFileUri!!,
                onSuccess = {
                    selectedFileUri = null
                    coroutineScope.launch {
                        userViewModel.refresh()
                        snackbarHostState.showSnackbar("Study plan uploaded successfully")
                    }
                }
            )
            user?.let { userViewModel.updateStudyPlanObservation(it.id, "") }
            user?.let { userViewModel.updateStudyPlanReviewed(it.id, false) }
        }
    }

    fun handleDownload() {
        coroutineScope.launch {
            studyPlanViewModel.downloadStudyPlan(context, url = user!!.studyPlan)
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
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "You need to download the template, fill it in with the necessary details, and upload it. This section can be edited until the end date",
                textAlign = TextAlign.Justify,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                ),
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("https://docs.google.com/document/d/1OFHJNiesQ69wmgmxHMYSJx8S6ZaaChuK/export?format=pdf")
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

                    context.startActivity(intent)
                },
                modifier = Modifier
                    .wrapContentWidth(align = Alignment.CenterHorizontally)
                    .width(120.dp)
                    .height(35.dp)
            ) {
                Text(
                    color = Color.White,
                    text = "Template"
                )
            }

            if (user!!.studyPlan != "") {
                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(text = "You have already uploaded a file")

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { handleDownload() },
                        modifier = Modifier
                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                            .width(120.dp)
                            .height(35.dp)
                    ) {
                        Text(
                            color = Color.White,
                            text = "Download"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            if(!user!!.studyPlanAccepted) {
                Button(
                    onClick = { launcher.launch("application/pdf") },
                    modifier = Modifier
                        .height(72.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Icon(
                            painter = painterResource(R.drawable.outline_file_upload_24),
                            contentDescription = "Upload",
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        selectedFileUri?.let {
                            Text(text = "Selected File: ${it.path}")
                        }

                        if (selectedFileUri == null) {
                            Text(text = "Upload Study Plan PDF")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    enabled = selectedFileUri != null,
                    onClick = { handleSave() },
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        top = 12.dp,
                        end = 12.dp,
                        bottom = 12.dp,
                    ),
                    modifier = Modifier
                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                        .width(100.dp)
                ) {
                    Text(
                        color = Color.White,
                        text = "Save"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            user!!.studyPlanObservation.let {
                if (it.isNotEmpty()) {
                    Text(
                        text = "Observation: $it",
                        textAlign = TextAlign.Justify,
                        style = TextStyle(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Red
                        ),
                    )
                }
            }
        }
    }
}
