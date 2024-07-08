package com.example.doubledegreeapp.services

import android.app.DownloadManager
import android.net.Uri
import android.os.Environment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import android.content.Context


class FileService {
    private val auth = FirebaseAuth.getInstance()
    private val storage = Firebase.storage

    private var storageRef = storage.reference

    suspend fun uploadFile(uri: Uri): String {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            throw Error("Nenhum usuário ativo")
        }

        val fileRef = storageRef.child("${currentUser.uid}/${uri.lastPathSegment}")

        return try {
            val uploadTask = fileRef.putFile(uri).await()
            fileRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            throw e
        }
    }
    suspend fun downloadFile(context: Context, filename: String, url: String) {
        try {
            val request = DownloadManager.Request(Uri.parse(url))
                .setTitle(filename)
                .setDescription("Downloading PDF file...")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${filename}.pdf")
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}