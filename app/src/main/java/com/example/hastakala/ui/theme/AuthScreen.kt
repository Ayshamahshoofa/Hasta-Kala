package com.example.hastakala.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hastakala.ui.theme.*

@Composable
fun AuthScreen(
    isLogin: Boolean,
    onSuccess: () -> Unit,
    onToggle: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isLogin) "Welcome Back" else "Create Account",
            fontSize = 28.sp,
            fontWeight = FontWeight.Black,
            color = BrandBrown
        )
        Spacer(modifier = Modifier.height(32.dp))

        if (!isLogin) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onSuccess,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandBrown),
            shape = RoundedCornerShape(16.dp),
            enabled = email.contains("@") && password.isNotEmpty() && (isLogin || name.isNotEmpty())
        ) {
            Text(if (isLogin) "LOGIN" else "REGISTER", fontWeight = FontWeight.Bold)
        }

        if (email.isNotEmpty() && !email.contains("@")) {
            Text(
                "Email must contain @",
                color = BrandRed,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        TextButton(onClick = onToggle) {
            Text(
                text = if (isLogin) "Don't have an account? Register" else "Already have an account? Login",
                color = ArtisanTan
            )
        }
    }
}
