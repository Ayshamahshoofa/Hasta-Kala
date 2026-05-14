package com.example.hastakala.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.hastakala.ui.theme.*

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2500)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ArtisanSand),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🎨", fontSize = 80.sp)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Hasta-Kala", color = BrandBrown, fontSize = 40.sp, fontWeight = FontWeight.Black)
            Text("ARTISAN TRADITION", color = ArtisanTan, fontSize = 14.sp, letterSpacing = 8.sp)

            Spacer(modifier = Modifier.height(48.dp))
            CircularProgressIndicator(color = BrandBrown, strokeWidth = 2.dp)
        }
    }
}
