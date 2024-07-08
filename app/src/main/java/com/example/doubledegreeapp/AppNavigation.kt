package com.example.doubledegreeapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.doubledegreeapp.viewmodel.CurriculumVitaeViewModel
import com.example.doubledegreeapp.viewmodel.LetterOfAcceptanceViewModel
import com.example.doubledegreeapp.viewmodel.PassportViewModel
import com.example.doubledegreeapp.viewmodel.UserViewModel
import com.example.doubledegreeapp.viewmodel.StudyPlanViewModel

@Preview(showBackground = true)
@Composable
fun AppNavigation() {
    val userViewModel: UserViewModel = viewModel()
    val curriculumVitaeViewModel: CurriculumVitaeViewModel = viewModel()
    val letterOfAcceptanceViewModel: LetterOfAcceptanceViewModel = viewModel()
    val studyPlanViewModel: StudyPlanViewModel = viewModel()
    val passportViewModel: PassportViewModel = viewModel()

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "WelcomeScreen") {
        composable("WelcomeScreen") { WelcomeScreen(navController) }
        composable("ProfessorsHomeScreen") { ProfessorsHomeScreen(navController, userViewModel) }
        composable("LoginScreen") { LoginScreen(navController, userViewModel) }
        composable("RegisterScreen") { RegisterScreen(navController, userViewModel) }
        composable("QualifiedStudentsScreen") { QualifiedStudentsScreen(navController, userViewModel) }
        composable("EditProfileScreen") { EditProfileScreen(navController, userViewModel) }
        composable("ProfileScreen") { ProfileScreen(navController, userViewModel) }
        composable("ForgotPasswordScreen") { ForgotPasswordScreen(navController) }
        composable("LetterOfAcceptanceScreen") { LetterOfAcceptanceScreen(navController, userViewModel) }
        composable("VisaScreen") { VisaScreen(navController) }
        composable("StudyPlanScreen") { StudyPlanScreen(navController, userViewModel) }
        composable("CurriculumVitaeScreen") { CurriculumVitaeScreen(navController, userViewModel) }
        composable("ProfessorProfileScreen") { ProfessorProfileScreen(navController) }
        composable("ProfessorPassportScreen") { ProfessorPassportScreen(navController, userViewModel, passportViewModel) }
        composable("HomeScreen") { HomeScreen(navController, userViewModel) }
        composable("ProfessorLetterOfAcceptanceScreen") { ProfessorLetterOfAcceptanceScreen(navController, userViewModel, letterOfAcceptanceViewModel) }
        composable("ProfessorStudyPlanScreen") { ProfessorStudyPlanScreen(navController, userViewModel, studyPlanViewModel) }
        composable("PassportScreen") { PassportScreen(navController, userViewModel) }
        composable("ProfessorCurriculumVitaeScreen") { ProfessorCurriculumVitaeScreen(navController, userViewModel, curriculumVitaeViewModel) }
        composable("ProfessorsChatsScreen") { ProfessorsChatsScreen(navController, userViewModel) }
    }
}
