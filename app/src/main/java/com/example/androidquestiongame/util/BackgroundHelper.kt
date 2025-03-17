package com.example.androidquestiongame.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import android.content.Context
import java.io.File

/**
 * Helper class for managing background images from the file system
 */
object BackgroundHelper {
    
    /**
     * Set a background from the local file system
     */
    fun setBackgroundFromFile(context: Context, screen: Screen, filePath: String) {
        val file = File(filePath)
        if (file.exists() && file.isFile) {
            // Get file's content URI
            val uri = android.net.Uri.fromFile(file)
            
            // Extract resource ID if it's drawable resource path
            val resourceName = file.nameWithoutExtension
            val resourceId = context.resources.getIdentifier(
                resourceName, "drawable", context.packageName
            )
            
            if (resourceId != 0) {
                // It's a drawable resource
                BackgroundManager.setLocalBackground(screen, resourceId)
            } else {
                // It's a file URL
                BackgroundManager.setWebBackground(screen, uri.toString())
            }
        }
    }
    
    /**
     * Set a background from a web URL
     */
    fun setBackgroundFromWeb(screen: Screen, url: String) {
        if (url.startsWith("http")) {
            BackgroundManager.setWebBackground(screen, url)
        }
    }
    
    /**
     * Creates custom background directories for the app
     */
    fun createBackgroundDirectories(context: Context): File {
        val backgroundsDir = File(context.filesDir, "backgrounds")
        if (!backgroundsDir.exists()) {
            backgroundsDir.mkdir()
            
            // Create subdirectories for each screen
            File(backgroundsDir, "home").mkdir()
            File(backgroundsDir, "practice").mkdir()
            File(backgroundsDir, "challenge").mkdir()
            File(backgroundsDir, "expert").mkdir()
            File(backgroundsDir, "results").mkdir()
            
            // Create a README file with instructions
            val readmeFile = File(backgroundsDir, "README.txt")
            readmeFile.writeText(
                """
                BACKGROUND IMAGES DIRECTORY
                
                Place your custom background images in these folders:
                
                /backgrounds/home/ - For the home screen
                /backgrounds/practice/ - For practice mode
                /backgrounds/challenge/ - For challenge mode
                /backgrounds/expert/ - For expert mode
                /backgrounds/results/ - For results screen
                
                You can use both image files (JPG, PNG) and URLs.
                To use a URL, create a text file with .url extension and place the URL inside.
                
                Examples:
                /backgrounds/home/my_home_bg.jpg - Local image file
                /backgrounds/practice/online_bg.url - Text file containing a URL
                """.trimIndent()
            )
        }
        return backgroundsDir
    }
} 