package com.example.doubledegreeapp

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Surface
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.doubledegreeapp.viewmodel.LetterOfAcceptanceViewModel
import com.example.doubledegreeapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch

@Composable
fun LetterOfAcceptanceScreen(navController: NavHostController, userViewModel: UserViewModel) {
    val letterOfAcceptanceViewModel: LetterOfAcceptanceViewModel = viewModel()

    val context = LocalContext.current

    val user by userViewModel.user.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollState()

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    println(selectedFileUri?.path)

    fun handleDownload () {
        coroutineScope.launch {
            letterOfAcceptanceViewModel.downloadLetterOfAcceptance(context, url = user!!.letterOfAcceptance)
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
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black,
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Letter of Acceptance",
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
                text = "The letter of acceptance is a document that confirm your acceptation by destination university. You can do the download when is available.",
                textAlign = TextAlign.Justify,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                ),
            )

            if(user!!.letterOfAcceptance != "") {
                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Text(text = "Your letter of acceptance is available")

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { handleDownload() },
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
                                painter = painterResource(R.drawable.outline_file_download_24),
                                contentDescription = "Download",
                                modifier = Modifier.size(40.dp)
                            )
                            Text(text = "Letter of Acceptance")
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }else{
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(text = "Your letter of acceptance is not available yet.")
                }
            }
        }
    }
}