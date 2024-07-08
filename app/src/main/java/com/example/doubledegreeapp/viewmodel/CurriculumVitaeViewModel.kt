package com.example.doubledegreeapp.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.doubledegreeapp.services.FileService
import com.example.doubledegreeapp.services.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CurriculumVitaeViewModel: ViewModel() {
    private val fileService = FileService()
    private val userService = UserService()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading = _loading.asStateFlow()

    suspend fun uploadCurriculum (uri: Uri, onSuccess: (String) -> Unit) {
        try {
            _loading.value = true

            val curriculumUrl = fileService.uploadFile(uri)

            userService.addCurriculum(curriculumUrl)

            onSuccess(curriculumUrl)
        } catch (e: Error) {
            println(e)
        } finally {
            _loading.value = false
        }
    }

    suspend fun downloadCurriculum (context: Context, url: String) {
        fileService.downloadFile(context = context, filename = "Curriculum", url)
    }
}