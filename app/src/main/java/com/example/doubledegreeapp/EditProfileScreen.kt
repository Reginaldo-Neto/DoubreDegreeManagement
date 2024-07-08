package com.example.doubledegreeapp

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.example.doubledegreeapp.viewmodel.UserViewModel
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import android.util.Log
import androidx.compose.foundation.background
import kotlinx.coroutines.tasks.await

@Composable
fun EditProfileScreen(navController: NavHostController, userViewModel: UserViewModel) {
    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()

    var isNameValid by remember { mutableStateOf(false) }
    var isPhoneNumberValid by remember { mutableStateOf(false) }
    var isDegreeValid by remember { mutableStateOf(false) }

    val storage = Firebase.storage
    val storageRef = storage.reference

    val user by userViewModel.user.collectAsState()

    var selectedImageUri by remember { mutableStateOf(user?.profileImage) }

    var newName by remember { mutableStateOf(user?.name ?: "") }
    var newPhone by remember { mutableStateOf(user?.phone ?: "") }
    var newDegree by remember { mutableStateOf(user?.degree ?: "") }

    val context = LocalContext.current

    fun createTempFileFromContentUri(uri: Uri): File {
        val tempFile = File.createTempFile("temp_image", ".jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }
        return tempFile
    }

suspend fun uploadImageToFirebase(uri: Uri): String? {
    return try {
        val tempFile = createTempFileFromContentUri(uri)

        val imageRef = storageRef.child("profile_images/${UUID.randomUUID()}")
        Log.d("FirebaseStorage", "Path of imageRef: ${imageRef.path}")

        val uploadTask = imageRef.putFile(Uri.fromFile(tempFile))
        val taskSnapshot = uploadTask.await()

        val downloadUrl = imageRef.downloadUrl.await()
        val imageUrl = downloadUrl.toString()

        tempFile.delete()
        imageUrl
    } catch (e: Exception) {
        Log.e("FirebaseStorage", "Error uploading image: ${e.message}", e)
        null
    }
}


    fun handleUpdateUser() {
        coroutineScope.launch {
            try {
                selectedImageUri?.let { uri ->
                    val imageUrl = uploadImageToFirebase(Uri.parse(uri))
                    if (imageUrl != null) {
                        userViewModel.updateUser(
                            newName = newName,
                            newPhone = newPhone,
                            newDegree = newDegree,
                            profileImage = imageUrl
                        )
                    } else {
                        Log.e("EditProfileScreen", "Failed to upload image or get URL")
                    }
                }
                navController.popBackStack()
            } catch (e: Exception) {
                Log.e("EditProfileScreen", "Error updating user: ${e.message}", e)
            }
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it.toString()
        }
    }

    Surface(
        color = Color("#EDEAF0".toColorInt()),
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        focusManager.clearFocus()
                    })
                }
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(scrollState)
                    .imePadding()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .clickable { imagePickerLauncher.launch("image/*") }
                ) {
                    if (selectedImageUri != null && selectedImageUri!!.isNotEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(model = selectedImageUri),
                            contentDescription = "Selected Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                                .align(Alignment.Center)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                                contentDescription = "Upload Image",
                                modifier = Modifier
                                    .size(48.dp)
                                    .align(Alignment.Center)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                NameInputField(
                    name = newName,
                    onNameChange = { newName = it },
                    isValid = { isNameValid = it },
                    onImeAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))

                PhoneNumberInputField(
                    phoneNumber = newPhone,
                    onPhoneNumberChange = { newPhone = it },
                    isValid = { isPhoneNumberValid = it },
                    onImeAction = {
                        focusManager.moveFocus(FocusDirection.Down)
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                DegreeInputField(
                    degree = newDegree,
                    onDegreeChange = { newDegree = it },
                    isValid = { isDegreeValid = it },
                    onImeAction = {
                        focusManager.clearFocus()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { handleUpdateUser()},
                    enabled = isNameValid && isPhoneNumberValid,
                    contentPadding = PaddingValues(
                        start = 12.dp,
                        top = 12.dp,
                        end = 12.dp,
                        bottom = 12.dp,
                    ),

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .width(227.dp)
                ) {
                    Text(
                        color = Color.White,
                        text = "Save Changes"
                    )
                }
            }
            IconButton(
                onClick = { navController.popBackStack() },
                Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 18.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black,
                )
            }
        }
    }
}