package com.example.androidquestiongame.util

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidquestiongame.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image

/**
 * Manages background images for different screens in the app.
 * Supports both local drawables and web URLs.
 */
object BackgroundManager {
    // Source types for background images
    enum class ImageSource {
        LOCAL_DRAWABLE,   // From res/drawable folder
        WEB_URL           // From a web URL
    }
    
    // Background image data class
    data class BackgroundImage(
        val source: ImageSource = ImageSource.LOCAL_DRAWABLE,
        val localResourceId: Int = R.drawable.default_background,
        val webUrl: String = ""
    )
    
    // Default backgrounds for different screens
    private val defaultHomeBackground = BackgroundImage(
        source = ImageSource.LOCAL_DRAWABLE,
        localResourceId = R.drawable.home_background
    )
    
    private val defaultPracticeBackground = BackgroundImage(
        source = ImageSource.LOCAL_DRAWABLE,
        localResourceId = R.drawable.practice_background
    )
    
    private val defaultChallengeBackground = BackgroundImage(
        source = ImageSource.LOCAL_DRAWABLE,
        localResourceId = R.drawable.challenge_background
    )
    
    private val defaultExpertBackground = BackgroundImage(
        source = ImageSource.LOCAL_DRAWABLE,
        localResourceId = R.drawable.expert_background
    )
    
    private val defaultResultsBackground = BackgroundImage(
        source = ImageSource.LOCAL_DRAWABLE,
        localResourceId = R.drawable.results_background
    )
    
    // Mutable states for each screen background
    var homeBackground = mutableStateOf(defaultHomeBackground)
    var practiceBackground = mutableStateOf(defaultPracticeBackground)
    var challengeBackground = mutableStateOf(defaultChallengeBackground)
    var expertBackground = mutableStateOf(defaultExpertBackground)
    var resultsBackground = mutableStateOf(defaultResultsBackground)
    
    /**
     * Set a local drawable resource as background for a specific screen
     */
    fun setLocalBackground(screen: Screen, resourceId: Int) {
        when (screen) {
            Screen.HOME -> homeBackground.value = BackgroundImage(
                source = ImageSource.LOCAL_DRAWABLE,
                localResourceId = resourceId
            )
            Screen.PRACTICE -> practiceBackground.value = BackgroundImage(
                source = ImageSource.LOCAL_DRAWABLE,
                localResourceId = resourceId
            )
            Screen.CHALLENGE -> challengeBackground.value = BackgroundImage(
                source = ImageSource.LOCAL_DRAWABLE,
                localResourceId = resourceId
            )
            Screen.EXPERT -> expertBackground.value = BackgroundImage(
                source = ImageSource.LOCAL_DRAWABLE,
                localResourceId = resourceId
            )
            Screen.RESULTS -> resultsBackground.value = BackgroundImage(
                source = ImageSource.LOCAL_DRAWABLE,
                localResourceId = resourceId
            )
        }
    }
    
    /**
     * Set a web URL as background for a specific screen
     */
    fun setWebBackground(screen: Screen, url: String) {
        when (screen) {
            Screen.HOME -> homeBackground.value = BackgroundImage(
                source = ImageSource.WEB_URL,
                webUrl = url
            )
            Screen.PRACTICE -> practiceBackground.value = BackgroundImage(
                source = ImageSource.WEB_URL,
                webUrl = url
            )
            Screen.CHALLENGE -> challengeBackground.value = BackgroundImage(
                source = ImageSource.WEB_URL,
                webUrl = url
            )
            Screen.EXPERT -> expertBackground.value = BackgroundImage(
                source = ImageSource.WEB_URL,
                webUrl = url
            )
            Screen.RESULTS -> resultsBackground.value = BackgroundImage(
                source = ImageSource.WEB_URL,
                webUrl = url
            )
        }
    }
    
    /**
     * Reset a screen background to its default
     */
    fun resetBackground(screen: Screen) {
        when (screen) {
            Screen.HOME -> homeBackground.value = defaultHomeBackground
            Screen.PRACTICE -> practiceBackground.value = defaultPracticeBackground
            Screen.CHALLENGE -> challengeBackground.value = defaultChallengeBackground
            Screen.EXPERT -> expertBackground.value = defaultExpertBackground
            Screen.RESULTS -> resultsBackground.value = defaultResultsBackground
        }
    }
    
    /**
     * Reset all backgrounds to defaults
     */
    fun resetAllBackgrounds() {
        homeBackground.value = defaultHomeBackground
        practiceBackground.value = defaultPracticeBackground
        challengeBackground.value = defaultChallengeBackground
        expertBackground.value = defaultExpertBackground
        resultsBackground.value = defaultResultsBackground
    }
    
    /**
     * Get the appropriate background for the current game level
     */
    fun getBackgroundForGameLevel(level: Int): MutableState<BackgroundImage> {
        return when (level) {
            0 -> practiceBackground
            1 -> challengeBackground
            2 -> expertBackground
            else -> practiceBackground
        }
    }
}

/**
 * Screens that can have custom backgrounds
 */
enum class Screen {
    HOME,
    PRACTICE,
    CHALLENGE,
    EXPERT,
    RESULTS
}

/**
 * Composable function to render a background from the BackgroundManager
 */
@Composable
fun BackgroundImage(
    backgroundImage: BackgroundManager.BackgroundImage,
    contentDescription: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    when (backgroundImage.source) {
        BackgroundManager.ImageSource.LOCAL_DRAWABLE -> {
            Image(
                painter = painterResource(
                    id = backgroundImage.localResourceId
                ),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }
        BackgroundManager.ImageSource.WEB_URL -> {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(backgroundImage.webUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = contentDescription,
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }
    }
} 