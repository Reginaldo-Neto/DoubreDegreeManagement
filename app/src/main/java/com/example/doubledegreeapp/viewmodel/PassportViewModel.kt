package com.example.doubledegreeapp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.doubledegreeapp.services.FileService
import com.example.doubledegreeapp.services.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PassportViewModel: ViewModel() {
    private val fileService = FileService()
    private val userService = UserService()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading = _loading.asStateFlow()

    suspend fun uploadPassport (uri: Uri, onSuccess: (String) -> Unit) {
        try {
            _loading.value = true

            val passportUrl = fileService.uploadFile(uri)

            userService.addPassport(passportUrl)

            onSuccess(passportUrl)
        } catch (e: Error) {
            println(e)
        } finally {
            _loading.value = false
        }
    }

    suspend fun downloadPassport (context: Context, url: String) {
        fileService.downloadFile(context = context, filename = "Passport", url)
    }
}