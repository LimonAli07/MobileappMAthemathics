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
    
    private var timerJob: Job? = null
    private var soundManager: SoundManager? = null

    fun initialize(context: Context) {
        soundManager = SoundManager(context)
    }

    fun startGame(level: Int) {
        gameLevel = level
        score = 0
        questionsAnswered = 0
        isGameOver = false
        nextQuestion()
    }

    private fun nextQuestion() {
        if (questionsAnswered >= 10) {
            isGameOver = true
            return
        }
        
        currentQuestion = generateQuestion()
        when (gameLevel) {
            1 -> startTimer(20)
            2 -> startTimer(10)
        }
    }

    private fun startTimer(seconds: Int) {
        timerJob?.cancel()
        timeLeft = seconds
        
        timerJob = viewModelScope.launch {
            while (timeLeft!! > 0) {
                delay(1000)
                timeLeft = timeLeft!! - 1
                if (timeLeft == 0) {
                    handleAnswer(null)
                }
            }
        }
    }

    fun handleAnswer(userAnswer: Int?) {
        timerJob?.cancel()
        if (userAnswer == currentQuestion?.answer) {
            score++
            soundManager?.playCorrectSound()
        } else {
            soundManager?.playIncorrectSound()
        }
        questionsAnswered++
        nextQuestion()
    }

    override fun onCleared() {
        super.onCleared()
        soundManager?.release()
        soundManager = null
    }
} 