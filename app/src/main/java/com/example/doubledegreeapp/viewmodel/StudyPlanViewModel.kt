package com.example.doubledegreeapp.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.doubledegreeapp.services.FileService
import com.example.doubledegreeapp.services.UserService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL

class StudyPlanViewModel: ViewModel() {
    private val fileService = FileService()
    private val userService = UserService()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading = _loading.asStateFlow()

    suspend fun uploadStudyPlan (uri: Uri, onSuccess: (String) -> Unit) {
        try {
            _loading.value = true

            val studyPlanUrl = fileService.uploadFile(uri)

            userService.addStudyPlan(studyPlanUrl)

            onSuccess(studyPlanUrl)
        } catch (e: Error) {
            println(e)
        } finally {
            _loading.value = false
        }
    }

    suspend fun downloadStudyPlan (context: Context, url: String) {
        fileService.downloadFile(context = context, filename = "StudyPlan", url)
    }
}