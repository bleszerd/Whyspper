package bleszerd.com.github.whyspper.controllers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever

class BitmapController {
    fun getAlbumImage(audioPath: String): Bitmap? {
        //create MediaMetadataRetriever instance
        val mediaRetriever = MediaMetadataRetriever()

        //pointer the MediaMetadataRetriever to a specific path
        mediaRetriever.setDataSource(audioPath)

        //get a album picture from metadata
        val data = mediaRetriever.embeddedPicture

        //can be used for extract a lot of others info's
        //val bitrate = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)

        //return the album cover img if exists, else return null
        //BitmapFactory.decodeByteArray get the DATA and parse into bitmap
        return if (data != null) BitmapFactory.decodeByteArray(data, 0, data.size) else null
    }
}