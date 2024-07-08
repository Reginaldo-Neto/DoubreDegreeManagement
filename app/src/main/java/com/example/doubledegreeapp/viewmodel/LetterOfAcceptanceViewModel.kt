package com.example.doubledegreeapp.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.doubledegreeapp.services.FileService
import com.example.doubledegreeapp.services.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LetterOfAcceptanceViewModel: ViewModel() {
    private val fileService = FileService()
    private val userService = UserService()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading = _loading.asStateFlow()

    suspend fun uploadLetterOfAcceptance(uri: Uri, onSuccess: (String) -> Unit, userId: String) {
        try {
            _loading.value = true

            val letterOfAcceptanceUrl = fileService.uploadFile(uri)

            userService.addLetterOfAcceptance(userId, letterOfAcceptanceUrl)

            onSuccess(letterOfAcceptanceUrl)
        } catch (e: Exception) {
            Log.e("LetterOfAcceptanceViewModel", "Error uploading letter of acceptance", e)
        } finally {
            _loading.value = false
        }
    }

    suspend fun downloadLetterOfAcceptance (context: Context, url: String) {
        fileService.downloadFile(context = context, filename = "LetterOfAcceptance", url)
    }
}