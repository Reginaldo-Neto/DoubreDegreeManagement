package com.example.doubledegreeapp.services

import com.example.doubledegreeapp.models.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.tasks.await

class UserService {
    private val db = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    suspend fun getUserById (id: String): UserModel? {
        try {
            val result = db.collection("users")
                .whereEqualTo("id", id)
                .get()
                .await()

            if(result.isEmpty) {
                return null
            }

            return result.documents[0].toObject(UserModel::class.java)
        } catch (e: Error) {
            return null
        }
    }

    suspend fun createUser (user: UserModel): UserModel {
        try {
            db.collection("users").document(user.id).set(user).await()
            return user
        } catch (e: Error) {
            throw Error("Falha ao registrar o usuário")
        }
    }

    suspend fun addCurriculum (curriculumUrl: String) {
        if (firebaseAuth.currentUser == null) throw Error("Nenhum usuário ativo")

        val curriculum = hashMapOf(
            "curriculum" to curriculumUrl,
        )

        db.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .set(curriculum, SetOptions.merge()).await()
    }

    suspend fun addLetterOfAcceptance(userId: String, letterOfAcceptanceUrl: String) {
        val letterOfAcceptance = hashMapOf(
            "letterOfAcceptance" to letterOfAcceptanceUrl,
        )

        db.collection("users")
            .document(userId)
            .set(letterOfAcceptance, SetOptions.merge())
            .await()
    }

    suspend fun addPassport (passportUrl: String) {
        if (firebaseAuth.currentUser == null) throw Error("Nenhum usuário ativo")

        val passport = hashMapOf(
            "passport" to passportUrl,
        )

        db.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .set(passport, SetOptions.merge()).await()
    }

    suspend fun addStudyPlan (passportUrl: String) {
        if (firebaseAuth.currentUser == null) throw Error("Nenhum usuário ativo")

        val studyPlan = hashMapOf(
            "studyPlan" to passportUrl,
        )

        db.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .set(studyPlan, SetOptions.merge()).await()
    }

    suspend fun updateUser(user: UserModel) {
        try {
            db.collection("users").document(user.id).set(user).await()
        } catch (e: Exception) {
            throw Error("Falha ao atualizar o usuário: ${e.message}")
        }
    }

    suspend fun getUsersByProfAttribute(prof: Boolean): List<UserModel> {
        try {
            val result = db.collection("users")
                .whereEqualTo("prof", prof)
                .get()
                .await()

            return result.documents.mapNotNull { it.toObject(UserModel::class.java) }
        } catch (e: Exception) {
            throw Error("Falha ao obter usuários: ${e.message}")
        }
    }

    suspend fun getUsersWithCurriculum(): List<UserModel> {
        try {
            val result = db.collection("users")
                .whereNotEqualTo("curriculum", "")
                .get()
                .await()

            return result.documents.mapNotNull { it.toObject(UserModel::class.java) }
        } catch (e: Exception) {
            throw Error("Falha ao obter usuários: ${e.message}")
        }
    }
    suspend fun getUsersWithoutCurriculum(): List<UserModel> {
        try {
            val result = db.collection("users")
                .whereEqualTo("curriculum", "")
                .get()
                .await()

            return result.documents.mapNotNull { it.toObject(UserModel::class.java) }
        } catch (e: Exception) {
            throw Error("Falha ao obter usuários: ${e.message}")
        }
    }

    suspend fun getUsersWithStudyPlan(): List<UserModel> {
        try {
            val result = db.collection("users")
                .whereNotEqualTo("studyPlan", "")
                .get()
                .await()

            return result.documents.mapNotNull { it.toObject(UserModel::class.java) }
        } catch (e: Exception) {
            throw Error("Falha ao obter usuários: ${e.message}")
        }
    }

    suspend fun getUsersWithoutStudyPlan(): List<UserModel> {
        try {
            val result = db.collection("users")
                .whereEqualTo("studyPlan", "")
                .get()
                .await()

            return result.documents.mapNotNull { it.toObject(UserModel::class.java) }
        } catch (e: Exception) {
            throw Error("Falha ao obter usuários: ${e.message}")
        }
    }

    suspend fun clearCurriculum(userId: String) {
        try {
            val userData = hashMapOf(
                "curriculum" to ""
            )

            db.collection("users")
                .document(userId)
                .set(userData, SetOptions.merge())
                .await()
        } catch (e: Exception) {
            throw Error("Falha ao limpar o currículo do usuário: ${e.message}")
        }
    }

    suspend fun clearStudyPlan(userId: String) {
        try {
            val userData = hashMapOf(
                "studyPlan" to ""
            )
            db.collection("users")
                .document(userId)
                .set(userData, SetOptions.merge())
                .await()
        } catch (e: Exception) {
            throw Error("Falha ao limpar o study plan do usuário: ${e.message}")
        }
    }

    suspend fun getUsersWithPassport(): List<UserModel> {
        try {
            val result = db.collection("users")
                .whereNotEqualTo("passport", "")
                .get()
                .await()

            return result.documents.mapNotNull { it.toObject(UserModel::class.java) }
        } catch (e: Exception) {
            throw Error("Falha ao obter usuários: ${e.message}")
        }
    }

    suspend fun getUsersWithoutPassport(): List<UserModel> {
        try {
            val result = db.collection("users")
                .whereEqualTo("passport", "")
                .get()
                .await()

            return result.documents.mapNotNull { it.toObject(UserModel::class.java) }
        } catch (e: Exception) {
            throw Error("Falha ao obter usuários: ${e.message}")
        }
    }

    suspend fun clearPassport(userId: String) {
        try {
            val userData = hashMapOf(
                "passport" to ""
            )

            db.collection("users")
                .document(userId)
                .set(userData, SetOptions.merge())
                .await()
        } catch (e: Exception) {
            throw Error("Falha ao limpar o passport do usuário: ${e.message}")
        }
    }

    suspend fun clearLetterOfAcceptance(userId: String) {
        try {
            val userData = hashMapOf(
                "letterOfAcceptance" to ""
            )

            db.collection("users")
                .document(userId)
                .set(userData, SetOptions.merge())
                .await()
        } catch (e: Exception) {
            throw Error("Falha ao limpar o letterOfAcceptance do usuário: ${e.message}")
        }
    }


    suspend fun getUsersWithDocuments(): List<UserModel> {
        try {
            val result = db.collection("users")
                .whereEqualTo("passportAccepted", true)
                .whereEqualTo("studyPlanAccepted", true)
                .whereEqualTo("curriculumAccepted", true)
                .get()
                .await()

            return result.documents.mapNotNull { it.toObject(UserModel::class.java) }
        } catch (e: Exception) {
            throw Error("Falha ao obter usuários com documentos: ${e.message}")
        }
    }

    suspend fun updatePassportObservation(userId: String, observation: String) {
        val userRef = db.collection("users").document(userId)
        userRef.update("passportObservation", observation).await()
    }

    suspend fun updateStudyPlanObservation(userId: String, observation: String) {
        val userRef = db.collection("users").document(userId)
        userRef.update("studyPlanObservation", observation).await()
    }

    suspend fun updateCurriculumObservation(userId: String, observation: String) {
        val userRef = db.collection("users").document(userId)
        userRef.update("curriculumObservation", observation).await()
    }
}
