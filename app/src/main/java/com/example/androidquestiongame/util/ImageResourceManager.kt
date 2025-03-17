package com.example.androidquestiongame.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.androidquestiongame.R

/**
 * Manages image resources for the app.
 * Makes it easy to customize app appearance without modifying code.
 */
object ImageResourceManager {
    // Customizable background colors
    var primaryBackgroundColor = mutableStateOf(Color(0xFF1976D2))
    var secondaryBackgroundColor = mutableStateOf(Color(0xFF64B5F6))
    
    // Main app logo - can be changed at runtime
    var appLogoResId = mutableStateOf(R.drawable.app_logo)
    
    // Background image for the home screen
    var homeBackgroundResId = mutableStateOf(R.drawable.home_background)
    
    // Category icons
    var practiceIconResId = mutableStateOf(R.drawable.ic_practice)
    var challengeIconResId = mutableStateOf(R.drawable.ic_challenge)
    var expertIconResId = mutableStateOf(R.drawable.ic_expert)
    
    // User profile image (if applicable)
    var userProfileImageResId = mutableStateOf(R.drawable.default_avatar)
    
    /**
     * Updates the app logo with a new resource
     */
    fun updateAppLogo(newLogoResId: Int) {
        appLogoResId.value = newLogoResId
    }
    
    /**
     * Updates the home background with a new resource
     */
    fun updateHomeBackground(newBackgroundResId: Int) {
        homeBackgroundResId.value = newBackgroundResId
    }
    
    /**
     * Updates theme colors
     */
    fun updateThemeColors(primary: Color, secondary: Color) {
        primaryBackgroundColor.value = primary
        secondaryBackgroundColor.value = secondary
    }
} 