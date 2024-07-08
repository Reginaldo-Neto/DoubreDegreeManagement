package com.example.doubledegreeapp.services

import com.example.doubledegreeapp.models.UserModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await

class AuthService {
    private val auth = FirebaseAuth.getInstance()
    private val userService = UserService()

    suspend fun login(email: String, password: String): UserModel? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            result.user?.let { userService.getUserById(it.uid) }
        } catch (e: FirebaseAuthInvalidUserException) {
            throw Error("Email not allowed")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            throw Error("Incorrect password")
        } catch (e: FirebaseAuthException) {
            throw Error("Authentication error: ${e.message}")
        } catch (e: Exception) {
            throw Error("Login failed: ${e.message}")
        }
    }

    suspend fun createLogin (email: String, password: String): AuthResult? {
        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun updatePassword (newPassword: String) {
        try {
            if(auth.currentUser == null) {
                throw Error()
            }

            auth.currentUser!!.updatePassword(newPassword)
        } catch (e: Error) {
            throw Error("Falha ao alterar a senha")
        }
    }

    suspend fun logout() {
        auth.signOut()
    }
}