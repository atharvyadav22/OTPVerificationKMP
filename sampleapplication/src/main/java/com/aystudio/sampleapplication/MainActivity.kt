package com.aystudio.sampleapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.aystudio.sampleapplication.ui.LibraryPreview
import com.aystudio.sampleapplication.ui.theme.OTPAuthenticationKMPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OTPAuthenticationKMPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    LibraryPreview(innerPadding)
                }
            }
        }
    }
}

