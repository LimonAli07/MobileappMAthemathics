package com.example.androidquestiongame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import com.example.androidquestiongame.viewmodel.GameViewModel
import com.example.androidquestiongame.model.Question
import com.example.androidquestiongame.ui.theme.AndroidQuestionGameTheme
import androidx.compose.foundation.layout.Arrangement

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: GameViewModel by viewModels()
        viewModel.initialize(this)
        
        setContent {
            AndroidQuestionGameTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameContent(viewModel)
                }
            }
        }
    }
}

@Composable
private fun GameContent(viewModel: GameViewModel) {
    when {
        viewModel.currentQuestion == null && !viewModel.isGameOver -> {
            LevelSelectionScreen(onLevelSelected = { viewModel.startGame(it) })
        }
        viewModel.isGameOver -> {
            GameOverScreen(
                score = viewModel.score,
                onPlayAgain = { viewModel.startGame(viewModel.gameLevel) }
            )
        }
        else -> {
            GameScreen(viewModel)
        }
    }
}

@Composable
fun LevelSelectionScreen(onLevelSelected: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
fun GameScreen(viewModel: GameViewModel) {
    var answer by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Score: ${viewModel.score}/${viewModel.questionsAnswered}",
            style = MaterialTheme.typography.headlineMedium
        )
        
        if (viewModel.timeLeft != null) {
            Text(
                text = "Time: ${viewModel.timeLeft}s",
                style = MaterialTheme.typography.headlineMedium
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        viewModel.currentQuestion?.let { question ->
            Text(
                text = "${question.firstNumber} ${question.operation.symbol} ${question.secondNumber} = ?",
                style = MaterialTheme.typography.headlineLarge
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        TextField(
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
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                viewModel.handleAnswer(answer.toIntOrNull())
                answer = ""
            }
        ) {
            Text("Submit")
        }
    }
}

@Composable
fun GameOverScreen(score: Int, onPlayAgain: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Game Over!",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Final Score: $score/10",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onPlayAgain) {
            Text("Play Again")
        }
    }
}