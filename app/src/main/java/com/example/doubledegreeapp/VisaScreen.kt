package com.example.doubledegreeapp

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt
import androidx.compose.material3.Surface
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun VisaScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()

    Surface(
        color = Color("#EDEAF0".toColorInt()),
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {navController.popBackStack()}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Visa",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color("#6750A4".toColorInt())
                    ),
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "The next step is to apply for the study visa at the Portuguese consulate in Brazil using your acceptance letter. You can search for the consulate nearest to you or access the link below for more information",
                textAlign = TextAlign.Justify,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                ),
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /* Handle Visa button click */ },
                modifier = Modifier
                    .height(72.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {

                    Icon(
                        painter = painterResource(R.drawable.baseline_link_24),
                        contentDescription = "Upload",
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(text = "More informations")
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun VisaScreenPreview() {
    val navController = rememberNavController()
    VisaScreen(navController)
}