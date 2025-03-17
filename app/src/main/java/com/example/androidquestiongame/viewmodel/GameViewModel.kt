package com.example.androidquestiongame.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.androidquestiongame.model.Question
import com.example.androidquestiongame.model.generateQuestion
import com.example.androidquestiongame.util.SoundManager
import java.time.LocalDate

class GameViewModel : ViewModel() {
    var currentQuestion by mutableStateOf<Question?>(null)
        private set
    var score by mutableStateOf(0)
        private set
    var questionsAnswered by mutableStateOf(0)
        private set
    var timeLeft by mutableStateOf<Int?>(null)
        private set
    var gameLevel by mutableStateOf(0)
        private set
    var isGameOver by mutableStateOf(false)
        private set
    var streak by mutableStateOf(0)
        private set
    private var lastPlayedDate: String? = null
    var showAnswerAnimation by mutableStateOf(false)
        private set
    var isCorrectAnswer by mutableStateOf(false)
        private set
    
    private var timerJob: Job? = null
    private var soundManager: SoundManager? = null
    private var totalTimeElapsed = 0

    fun initialize(context: Context) {
        soundManager = SoundManager(context)
        val prefs = context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
        streak = prefs.getInt("streak", 0)
        lastPlayedDate = prefs.getString("last_played_date", null)
        checkAndUpdateStreak(context)
    }

    fun startGame(level: Int) {
        gameLevel = level
        score = 0
        questionsAnswered = 0
        isGameOver = false
        totalTimeElapsed = 0
        generateNewQuestion()
        startTimer()
    }

    fun handleAnswer(answer: Int?) {
        if (answer == null || currentQuestion == null) return
        
        isCorrectAnswer = answer == currentQuestion?.answer
        showAnswerAnimation = true
        
        if (isCorrectAnswer) {
            score++
            soundManager?.playCorrectSound()
        } else {
            soundManager?.playIncorrectSound()
        }
        
        questionsAnswered++
        
        if (questionsAnswered >= 10) {
            isGameOver = true
            timerJob?.cancel()
        } else {
            generateNewQuestion()
            // Reset timer for the next question
            startTimer()
        }
    }

    private fun generateNewQuestion() {
        currentQuestion = generateQuestion()
    }

    private fun startTimer() {
        timerJob?.cancel()
        
        // Set time per question based on level
        when (gameLevel) {
            0 -> timeLeft = null  // No time limit
            1 -> timeLeft = 20    // 20 seconds per question
            2 -> timeLeft = 10    // 10 seconds per question
        }
        
        if (timeLeft != null) {
            timerJob = viewModelScope.launch {
                val questionStartTime = timeLeft
                while (timeLeft!! > 0 && !isGameOver) {
                    delay(1000)
                    timeLeft = timeLeft!! - 1
                    totalTimeElapsed++
                    
                    if (timeLeft == 0) {
                        // If time runs out for this question, move to next
                        // or end game if it was the last question
                        if (questionsAnswered >= 9) {
                            isGameOver = true
                        } else {
                            questionsAnswered++
                            generateNewQuestion()
                            // Reset timer for next question
                            timeLeft = questionStartTime
                        }
                    }
                }
            }
        }
    }

    private fun checkAndUpdateStreak(context: Context) {
        val today = LocalDate.now().toString()
        val yesterday = LocalDate.now().minusDays(1).toString()
        
        when (lastPlayedDate) {
            today -> return
            yesterday -> streak++
            else -> streak = 1
        }
        
        context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)
            .edit()
            .putInt("streak", streak)
            .putString("last_played_date", today)
            .apply()
    }

    override fun onCleared() {
        super.onCleared()
        soundManager?.release()
        soundManager = null
    }
} 