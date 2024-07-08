package com.example.doubledegreeapp.models

data class UserModel(
    val id: String = "",
    val prof: Boolean = false,
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val degree: String = "",
    val curriculum: String = "",
    val letterOfAcceptance: String = "",
    val passport: String = "",
    var profileImage: String = "",
    var studyPlan: String = "",
    val curriculumReviewed: Boolean = false,
    val curriculumAccepted: Boolean = false,
    val letterOfAcceptanceReviewed: Boolean = false,
    val letterOfAcceptanceAccepted: Boolean = false,
    val passportReviewed: Boolean = false,
    val passportAccepted: Boolean = false,
    val studyPlanReviewed: Boolean = false,
    val studyPlanAccepted: Boolean = false,
    val curriculumObservation: String = "",
    val studyPlanObservation: String = "",
    val passportObservation: String = ""
)