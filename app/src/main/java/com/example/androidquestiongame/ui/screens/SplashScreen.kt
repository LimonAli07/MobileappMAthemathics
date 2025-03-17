package com.example.androidquestiongame.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidquestiongame.R
import kotlinx.coroutines.delay
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun SplashScreen(onSplashComplete: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    val scale = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1000)
    )
    
    val rotation = animateFloatAsState(
        targetValue = if (startAnimation) 360f else 0f,
        animationSpec = tween(1000)
    )

    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(2000)
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFF1976D2),
                    Color(0xFF64B5F6)
                )
            )),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp)
                    .scale(scale.value)
                    .rotate(rotation.value)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "ARITHMECHIKS \n Limon Ali",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.scale(scale.value)
            )
        }
    }
} 