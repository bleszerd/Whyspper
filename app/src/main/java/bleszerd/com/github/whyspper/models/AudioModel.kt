package bleszerd.com.github.whyspper.models

import android.graphics.Bitmap
import android.net.Uri
import java.time.Duration

//audio data model
data class AudioModel(
    val title: String,
    val artist: String,
    var favorite: Boolean,
    val location: String,
    val albumArt: Bitmap?,
    val audioId: String
)