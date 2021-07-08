package bleszerd.com.github.whyspper.models

import android.graphics.Bitmap
import android.net.Uri
import java.time.Duration

data class AudioModel(
    val title: String,
    val location: String,
    val art: Bitmap?
)