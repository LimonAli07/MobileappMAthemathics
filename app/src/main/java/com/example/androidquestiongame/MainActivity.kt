package com.example.androidquestiongame

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.androidquestiongame.ui.theme.AndroidQuestionGameTheme
import com.example.androidquestiongame.viewmodel.GameViewModel
import com.example.androidquestiongame.ui.screens.HomeScreen
import com.example.androidquestiongame.ui.screens.SplashScreen
import com.example.androidquestiongame.util.BackgroundManager
import com.example.androidquestiongame.util.Screen
import com.example.androidquestiongame.util.BackgroundImage
import com.example.androidquestiongame.util.BackgroundHelper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: GameViewModel by viewModels()
        viewModel.initialize(this)
        
        // Create background directories
        BackgroundHelper.createBackgroundDirectories(this)
        
        // Initialize backgrounds
        BackgroundManager.setWebBackground(Screen.HOME, 
            "https://i.etsystatic.com/14675479/r/il/e042ad/5550494258/il_794xN.5550494258_6kzr.jpg")
        BackgroundManager.setWebBackground(Screen.PRACTICE, 
            "https://i.pinimg.com/236x/5f/24/26/5f24265314bf2c6f3a27a48781add464.jpg")
        BackgroundManager.setLocalBackground(Screen.CHALLENGE, R.drawable.challenge_background)
        BackgroundManager.setLocalBackground(Screen.EXPERT, R.drawable.expert_background)
        BackgroundManager.setLocalBackground(Screen.RESULTS, R.drawable.results_background)
        
        // Add background for final score screen
        BackgroundManager.setWebBackground(Screen.RESULTS, 
            "https://static01.nyt.com/images/2020/01/28/multimedia/28xp-memekid3/28cp-memekid3-superJumbo.jpg")
        
        // Get username from shared preferences or use default
        val prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userName = prefs.getString("user_name", "Math Learner") ?: "Math Learner"
        
        setContent {
            var showSplash by remember { mutableStateOf(true) }
            var currentScreen by remember { mutableStateOf(AppScreen.HOME) }
            
            AndroidQuestionGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when {
                        showSplash -> {
                            SplashScreen {
                                showSplash = false
                            }
                        }
                        else -> {
                            when (currentScreen) {
                                AppScreen.HOME -> {
                                    HomeScreen(
                                        viewModel = viewModel,
                                        onStartGame = { level ->
                                            viewModel.startGame(level)
                                            currentScreen = AppScreen.GAME
                                        },
                                        userName = userName
                                    )
                                }
                                AppScreen.GAME -> {
                                    GameContent(
                                        viewModel = viewModel,
                                        onBackToHome = {
                                            currentScreen = AppScreen.HOME
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private enum class AppScreen {
    HOME, 
    GAME
}

@Composable
private fun GameContent(
    viewModel: GameViewModel,
    onBackToHome: () -> Unit
) {
    when {
        viewModel.currentQuestion == null && !viewModel.isGameOver -> {
            LevelSelectionScreen(
                onLevelSelected = { viewModel.startGame(it) },
                onBack = onBackToHome
            )
        }
        viewModel.isGameOver -> {
            GameOverScreen(
                score = viewModel.score,
                onPlayAgain = { viewModel.startGame(viewModel.gameLevel) },
                onBackToHome = onBackToHome
            )
        }
        else -> {
            GameScreen(
                viewModel = viewModel,
                onBackToHome = onBackToHome
            )
        }
    }
}

@Composable
fun LevelSelectionScreen(
    onLevelSelected: (Int) -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.align(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }
        
        Text(
            text = "Select Level",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = { onLevelSelected(0) }) {
            Text("Level 0 - No Time Limit")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onLevelSelected(1) }) {
            Text("Level 1 - 20 Seconds")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onLevelSelected(2) }) {
            Text("Level 2 - 10 Seconds")
        }
    }
}

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onBackToHome: () -> Unit
) {
    var answer by remember { mutableStateOf("") }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background image based on game level
        BackgroundImage(
            backgroundImage = BackgroundManager.getBackgroundForGameLevel(viewModel.gameLevel).value,
            contentDescription = "Game Background",
            modifier = Modifier.fillMaxSize()
        )
        
        // Existing content wrapped in a Column with semi-transparent background
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.8f), shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackToHome) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back to Home"
                    )
                }
                Text(
                    text = "Question: ${viewModel.questionsAnswered + 1}/10",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            
            if (viewModel.timeLeft != null) {
    Text(
                    text = "Time: ${viewModel.timeLeft}s",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            viewModel.currentQuestion?.let { question ->
                Text(
                    text = question.text,
                    style = MaterialTheme.typography.headlineLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = answer,
                    onValueChange = { answer = it.filter { char -> char.isDigit() } },
                    label = { Text("Enter your answer") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            viewModel.handleAnswer(answer.toIntOrNull())
                            answer = ""
                        }
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = {
                        viewModel.handleAnswer(answer.toIntOrNull())
                        answer = ""
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Submit Answer")
                }
            }
        }
    }
}

@Composable
fun GameOverScreen(
    score: Int,
    onPlayAgain: () -> Unit,
    onBackToHome: () -> Unit
) {
    val percentage = (score * 100) / 10
    val backgroundColor = when {
        percentage >= 80 -> Color(0xFF388E3C) // Green for good performance
        percentage >= 60 -> Color(0xFF689F38) 
        percentage >= 40 -> Color(0xFFFFA000) // Yellow/Orange for medium
        else -> Color(0xFFD32F2F) // Red for needs improvement
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Results background image
        BackgroundImage(
            backgroundImage = BackgroundManager.resultsBackground.value,
            contentDescription = "Results Background",
            modifier = Modifier.fillMaxSize()
        )
        
        // Existing content wrapped in a Column with semi-transparent background
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.White.copy(alpha = 0.8f), shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with close button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Results",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
                
                IconButton(
                    onClick = onBackToHome,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Score display
            Text(
                text = "Your score",
                style = MaterialTheme.typography.titleMedium
            )
            
            Text(
                text = "$score/10",
                style = MaterialTheme.typography.headlineLarge,
                color = backgroundColor,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Percentage chart
            Box(
                modifier = Modifier.size(160.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = percentage / 100f,
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 16.dp,
                    color = backgroundColor
                )
                
                Text(
                    text = "$percentage%",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Time displays
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Time taken",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "2:34 mins",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Time remaining",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Text(
                        text = "7:26 mins",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Share section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Share your success",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Row {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share"
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "What to do next",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Text(
                text = "It's important to review your answers so you can see the areas you need to improve for next time.",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Play again button
            Button(
                onClick = onPlayAgain,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = backgroundColor)
            ) {
                Text("Play Again")
            }
        }
    }
}