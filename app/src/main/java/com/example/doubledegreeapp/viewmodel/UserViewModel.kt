package com.example.doubledegreeapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.doubledegreeapp.models.UserModel
import com.example.doubledegreeapp.services.AuthService
import com.example.doubledegreeapp.services.UserService
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel: ViewModel() {
    private val authService = AuthService()
    private val userService = UserService()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _user = MutableStateFlow<UserModel?>(null)
    val user = _user.asStateFlow()

    private val _loadingLogin = MutableStateFlow(false)
    val loadingLogin = _loadingLogin.asStateFlow()

    private val _loadingRefresh = MutableStateFlow(false)
    val loadingRefresh = _loadingRefresh.asStateFlow()

    private val _loadingRegister = MutableStateFlow(false)
    val loadingRegister = _loadingRegister.asStateFlow()

    private val _errorLogin = MutableStateFlow("")
    val errorLogin = _errorLogin.asStateFlow()

    private val _errorRegister = MutableStateFlow("")
    val errorRegister = _errorRegister.asStateFlow()

    private val _allStudents = MutableStateFlow<List<UserModel>>(emptyList())
    val allStudents = _allStudents.asStateFlow()

    private val _allProfessors = MutableStateFlow<List<UserModel>>(emptyList())
    val allProfessors = _allProfessors.asStateFlow()

    private val _studentsWithCurriculum = MutableStateFlow<List<UserModel>>(emptyList())
    val studentsWithCurriculum = _studentsWithCurriculum.asStateFlow()

    private val _studentsWithoutCurriculum = MutableStateFlow<List<UserModel>>(emptyList())
    val studentsWithoutCurriculum = _studentsWithoutCurriculum.asStateFlow()

    private val _studentsWithStudyPlan = MutableStateFlow<List<UserModel>>(emptyList())
    val studentsWithStudyPlan = _studentsWithStudyPlan.asStateFlow()

    private val _studentsWithoutStudyPlan = MutableStateFlow<List<UserModel>>(emptyList())
    val studentsWithoutStudyPlan = _studentsWithoutStudyPlan.asStateFlow()

    private val _studentsWithPassport = MutableStateFlow<List<UserModel>>(emptyList())
    val studentsWithPassport = _studentsWithPassport.asStateFlow()

    private val _studentsWithoutPassport = MutableStateFlow<List<UserModel>>(emptyList())
    val studentsWithoutPassport = _studentsWithoutPassport.asStateFlow()

    private val _studentsWithLetterOfAcceptance = MutableStateFlow<List<UserModel>>(emptyList())
    val studentsWithLetterOfAcceptance = _studentsWithLetterOfAcceptance.asStateFlow()

    private val _studentsWithDocuments = MutableStateFlow<List<UserModel>>(emptyList())
    val studentsWithDocuments = _studentsWithDocuments.asStateFlow()

    suspend fun login (email: String, password: String, onSuccess: (UserModel?) -> Unit) {
        try {
            _loadingLogin.value = true
            _errorLogin.value = ""

            val authenticatedUser = authService.login(email = email, password = password)

            _user.value = authenticatedUser

            onSuccess(authenticatedUser)

            return
        } catch (e: Error) {
            _errorLogin.value = e.message.toString()
        } finally {
            _loadingLogin.value = false
        }
    }

    suspend fun logout() {
        try {
            authService.logout()
            _user.value = null // Limpar o estado do usuário no ViewModel após o logout
        } catch (e: Exception) {
            throw Error("Erro ao fazer logout: ${e.message}")
        }
    }

    suspend fun register (
        name: String,
        phone: String,
        degree: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        try {
            _loadingRegister.value = true
            _errorRegister.value = ""

            val id = firebaseAuth.currentUser?.uid ?: throw Error("Nenhum usuário autenticado")
            val email = firebaseAuth.currentUser?.email ?: throw Error("Nenhum usuário autenticado")

            val userToRegister = UserModel(
                id,
                prof = false,
                name,
                email,
                phone,
                degree,
                curriculum = "",
            )

            userService.createUser(userToRegister)

            authService.updatePassword(newPassword = password)

            _user.value = userToRegister

            onSuccess()

            return
        } catch (e: Error) {
            _errorRegister.value = e.message.toString()
        } finally {
            _loadingRegister.value = false
        }
    }

    suspend fun refresh () {
        try {
            _loadingRefresh.value = true

            if (firebaseAuth.currentUser == null) throw Error("Nenhum usuário ativo")

            val refreshedUser = userService.getUserById(firebaseAuth.currentUser!!.uid)

            _user.value = refreshedUser
        } catch (e: Error) {
            println(e)
        } finally {
            _loadingRefresh.value = false
        }
    }

    suspend fun updateUser(newName: String, newPhone: String, newDegree: String, profileImage: String) {
        try {
            _user.value?.let { currentUser ->
                val updatedUser = currentUser.copy(
                    name = newName,
                    phone = newPhone,
                    degree = newDegree,
                    profileImage = profileImage
                )
                userService.updateUser(updatedUser)
                _user.value = updatedUser
            }
        } catch (_: Exception) {
        }
    }

    suspend fun fetchStudents(function: () -> Unit) {
        try {
            val students = userService.getUsersByProfAttribute(false)
            _allStudents.value = students
        } catch (e: Exception) {
            println("Erro ao buscar estudantes: ${e.message}")
        }
    }

    suspend fun fetchProfessors(function: () -> Unit) {
        try {
            val professors = userService.getUsersByProfAttribute(true)
            _allProfessors.value = professors
        } catch (e: Exception) {
            println("Erro ao buscar professores: ${e.message}")
        }
    }

    suspend fun fetchUsersWithCurriculum(onSuccess: (List<UserModel>) -> Unit) {
        try {
            val users = userService.getUsersWithCurriculum()
            _studentsWithCurriculum.value = users
            onSuccess(users)
        } catch (e: Exception) {
            println("Erro ao obter usuários: ${e.message}")
        }
    }

    suspend fun fetchUsersWithoutCurriculum(onSuccess: (List<UserModel>) -> Unit) {
        try {
            val users = userService.getUsersWithoutCurriculum()
            _studentsWithoutCurriculum.value = users
            onSuccess(users)
        } catch (e: Exception) {
            println("Erro ao obter usuários: ${e.message}")
        }
    }

    suspend fun fetchUsersWithStudyPlan(onSuccess: (List<UserModel>) -> Unit) {
        try {
            val users = userService.getUsersWithStudyPlan()
            _studentsWithStudyPlan.value = users
            onSuccess(users)
        } catch (e: Exception) {
            println("Erro ao obter usuários: ${e.message}")
        }
    }

    suspend fun fetchUsersWithoutStudyPlan(onSuccess: (List<UserModel>) -> Unit) {
        try {
            val users = userService.getUsersWithoutStudyPlan()
            _studentsWithoutStudyPlan.value = users
            onSuccess(users)
        } catch (e: Exception) {
            println("Erro ao obter usuários: ${e.message}")
        }
    }

    suspend fun updateCurriculumReviewed(userId: String, reviewed: Boolean) {
        try {
            val user = userService.getUserById(userId)
            if (user != null) {
                val updatedUser = user.copy(curriculumReviewed = reviewed)
                userService.updateUser(updatedUser)
                if (userId == firebaseAuth.currentUser?.uid) {
                    _user.value = updatedUser
                }
            }
        } catch (e: Exception) {
            println("Erro ao atualizar currículo avaliado: ${e.message}")
        }
    }

    suspend fun updateCurriculumAccepted(userId: String, accepted: Boolean) {
        try {
            val user = userService.getUserById(userId)
            if (user != null) {
                val updatedUser = user.copy(curriculumAccepted = accepted)
                userService.updateUser(updatedUser)
                if (userId == firebaseAuth.currentUser?.uid) {
                    _user.value = updatedUser
                }
            }
        } catch (e: Exception) {
            println("Erro ao atualizar currículo aceito: ${e.message}")
        }
    }

    suspend fun updateStudyPlanReviewed(userId: String, reviewed: Boolean) {
        try {
            val user = userService.getUserById(userId)
            if (user != null) {
                val updatedUser = user.copy(studyPlanReviewed = reviewed)
                userService.updateUser(updatedUser)
                if (userId == firebaseAuth.currentUser?.uid) {
                    _user.value = updatedUser
                }
            }
        } catch (e: Exception) {
            println("Erro ao atualizar currículo avaliado: ${e.message}")
        }
    }

    suspend fun updateStudyPlanAccepted(userId: String, accepted: Boolean) {
        try {
            val user = userService.getUserById(userId)
            if (user != null) {
                val updatedUser = user.copy(studyPlanAccepted = accepted)
                userService.updateUser(updatedUser)
                if (userId == firebaseAuth.currentUser?.uid) {
                    _user.value = updatedUser
                }
            }
        } catch (e: Exception) {
            println("Erro ao atualizar currículo aceito: ${e.message}")
        }
    }

    suspend fun clearCurriculum(userId: String) {
        try {
            userService.clearCurriculum(userId)
            val updatedUsersWithCurriculum = _studentsWithCurriculum.value.map { user ->
                if (user.id == userId) {
                    user.copy(curriculum = "")
                } else {
                    user
                }
            }
            _studentsWithCurriculum.value = updatedUsersWithCurriculum
        } catch (e: Error) {
            println("Erro ao limpar currículo: ${e.message}")
        }
    }

    suspend fun clearStudyPlan(userId: String) {
        try {
            userService.clearStudyPlan(userId)
            val updatedUsersWithStudyPlan = _studentsWithStudyPlan.value.map { user ->
                if (user.id == userId) {
                    user.copy(studyPlan = "")
                } else {
                    user
                }
            }
            _studentsWithStudyPlan.value = updatedUsersWithStudyPlan
        } catch (e: Error) {
            println("Erro ao limpar currículo: ${e.message}")
        }
    }

    suspend fun clearLetterOfAcceptance(userId: String) {
        try {
            userService.clearLetterOfAcceptance(userId)
            val updatedUsersWithLetterOfAcceptance = _studentsWithLetterOfAcceptance.value.map { user ->
                if (user.id == userId) {
                    user.copy(letterOfAcceptance = "")
                } else {
                    user
                }
            }
            _studentsWithLetterOfAcceptance.value = updatedUsersWithLetterOfAcceptance
        } catch (e: Error) {
            println("Erro ao limpar currículo: ${e.message}")
        }
    }


    suspend fun fetchUsersWithPassport(onSuccess: (List<UserModel>) -> Unit) {
        try {
            val users = userService.getUsersWithPassport()
            _studentsWithPassport.value = users
            onSuccess(users)
        } catch (e: Exception) {
            println("Erro ao obter usuários: ${e.message}")
        }
    }

    suspend fun fetchUsersWithoutPassport(onSuccess: (List<UserModel>) -> Unit) {
        try {
            val users = userService.getUsersWithoutPassport()
            _studentsWithoutPassport.value = users
            onSuccess(users)
        } catch (e: Exception) {
            println("Erro ao obter usuários: ${e.message}")
        }
    }

    suspend fun updatePassportReviewed(userId: String, reviewed: Boolean) {
        try {
            val user = userService.getUserById(userId)
            if (user != null) {
                val updatedUser = user.copy(passportReviewed = reviewed)
                userService.updateUser(updatedUser)
                if (userId == firebaseAuth.currentUser?.uid) {
                    _user.value = updatedUser
                }
            }
        } catch (e: Exception) {
            println("Erro ao atualizar currículo avaliado: ${e.message}")
        }
    }

    suspend fun updatePassportAccepted(userId: String, accepted: Boolean) {
        try {
            val user = userService.getUserById(userId)
            if (user != null) {
                val updatedUser = user.copy(passportAccepted = accepted)
                userService.updateUser(updatedUser)
                if (userId == firebaseAuth.currentUser?.uid) {
                    _user.value = updatedUser
                }
            }
        } catch (e: Exception) {
            println("Erro ao atualizar currículo aceito: ${e.message}")
        }
    }

    suspend fun clearPassport(userId: String) {
        try {
            userService.clearPassport(userId)
            val updatedUsersWithPassport = _studentsWithPassport.value.map { user ->
                if (user.id == userId) {
                    user.copy(passport = "")
                } else {
                    user
                }
            }
            _studentsWithPassport.value = updatedUsersWithPassport
        } catch (e: Error) {
            println("Erro ao limpar currículo: ${e.message}")
        }
    }

    suspend fun fetchUsersWithDocuments(onSuccess: (List<UserModel>) -> Unit) {
        try {
            val users = userService.getUsersWithDocuments()
            _studentsWithDocuments.value = users
            onSuccess(users)
        } catch (e: Exception) {
            println("Erro ao obter usuários com documentos: ${e.message}")
        }
    }

    suspend fun updatePassportObservation(userId: String, observation: String) {
            try {
                userService.updatePassportObservation(userId, observation)
            } catch (e: Exception) {
                Log.e("UserViewModel", "Error updating passport observation", e)
            }
    }

    suspend fun updateCurriculumObservation(userId: String, observation: String) {
        try {
            userService.updateCurriculumObservation(userId, observation)
        } catch (e: Exception) {
            Log.e("UserViewModel", "Error updating curriculum observation", e)
        }
    }

    suspend fun updateStudyPlanObservation(userId: String, observation: String) {
        try {
            userService.updateStudyPlanObservation(userId, observation)
        } catch (e: Exception) {
            Log.e("UserViewModel", "Error updating study plan observation", e)
        }
    }
}