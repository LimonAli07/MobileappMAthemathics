package com.example.androidquestiongame.util

import android.content.Context
import android.media.MediaPlayer
import com.example.androidquestiongame.R

class SoundManager(private val context: Context) {
    private var correctSound: MediaPlayer? = null
    private var incorrectSound: MediaPlayer? = null
    
    init {
        correctSound = MediaPlayer.create(context, R.raw.correct)
        incorrectSound = MediaPlayer.create(context, R.raw.incorrect)
    }
    
    fun playCorrectSound() {
        correctSound?.start()
    }
    
    fun playIncorrectSound() {
        incorrectSound?.start()
    }
    
    fun release() {
        correctSound?.release()
        incorrectSound?.release()
        correctSound = null
        incorrectSound = null
    }
} 