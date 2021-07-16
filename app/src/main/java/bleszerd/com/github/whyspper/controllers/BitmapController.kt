package bleszerd.com.github.whyspper.controllers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import bleszerd.com.github.whyspper.R
import java.lang.Exception

class BitmapController {
    fun getAlbumImage(context: Context, audioPath: String): Bitmap? {
        val data: ByteArray?

        //create MediaMetadataRetriever instance
        val mediaRetriever = MediaMetadataRetriever()

        try {
            //pointer the MediaMetadataRetriever to a specific path
            mediaRetriever.setDataSource(audioPath)
        } catch (e: Exception){
            println(e.message)
            return null
        }


        //get a album picture from metadata
        data = mediaRetriever.embeddedPicture
        println(data.toString())
        //can be used for extract a lot of others info's
        //val bitrate = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)

        //return the album cover img if exists, else return null
        //BitmapFactory.decodeByteArray get the DATA and parse into bitmap
        return if (data != null)
            BitmapFactory.decodeByteArray(data, 0, data.size)
        else BitmapFactory.decodeResource(context.resources, R.drawable.no_image_cover, null)
    }
}