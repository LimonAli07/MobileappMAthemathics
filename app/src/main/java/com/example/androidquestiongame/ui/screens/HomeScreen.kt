package com.example.androidquestiongame.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.More
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidquestiongame.R
import com.example.androidquestiongame.util.BackgroundImage
import com.example.androidquestiongame.util.BackgroundManager
import com.example.androidquestiongame.util.Screen
import com.example.androidquestiongame.viewmodel.GameViewModel

@Composable
fun HomeScreen(
    viewModel: GameViewModel,
    onStartGame: (Int) -> Unit,
    userName: String = "User"
) {
    var selectedTab by remember { mutableStateOf(0) }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image using our new BackgroundManager
        BackgroundImage(
            backgroundImage = BackgroundManager.homeBackground.value,
            contentDescription = "Home Background",
            modifier = Modifier.fillMaxSize()
        )
        
        // Main content
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header with logo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(48.dp)
                )
                
                Text(
                    text = "ArithmeCHIKS",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
                
                Spacer(modifier = Modifier.weight(1f))
            }
            
            // Greeting
            Text(
                text = "Hey $userName,",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            
            Text(
                text = "You are practicing math questions",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Game modes as menu cards
            CategoryCard(
                title = "Practice Mode",
                icon = R.drawable.ic_practice,
                backgroundColor = Color(0xFF4CAF50),
                onClick = { onStartGame(0) }
            )
            
            CategoryCard(
                title = "Challenge Mode",
                icon = R.drawable.ic_challenge,
                backgroundColor = Color(0xFFFF9800),
                onClick = { onStartGame(1) }
            )
            
            CategoryCard(
                title = "Expert Mode",
                icon = R.drawable.ic_expert,
                backgroundColor = Color(0xFFF44336),
                onClick = { onStartGame(2) }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Bottom navigation bar with only Home, Share, Profile
            NavigationBar(
                modifier = Modifier.fillMaxWidth()
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.Share, contentDescription = "Share") },
                    label = { Text("Share") }
                )
                
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
                    label = { Text("Profile") }
                )
            }
        }
    }
}

@Composable
fun CategoryCard(
    title: String,
    icon: Int,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon with colored background
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(backgroundColor)
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = title,
                    modifier = Modifier.size(36.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.weight(1f))
        }
    }
} 