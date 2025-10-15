package com.example.diettracker.ui.components.modal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.diettracker.ui.components.buttons.CustomButton
import com.example.diettracker.ui.navigation.Screen

@Composable
fun WelcomeScreenModal(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
    ){
        CustomButton(
            text = "Login To Account",
            icon = Icons.Default.Mail,
            onClick = {
                navController.navigate(Screen.Login.route)
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        CustomButton(
            text = "Create New Account",
            icon = Icons.Default.PersonAdd,
            contentColor = Color(0xFF668405), // passes green color
            onClick = { navController.navigate(Screen.Register.route) }
        )
    }

}

