package bleszerd.com.github.whyspper.controllers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import bleszerd.com.github.whyspper.models.AudioModel

class AudioController {
    val bitmapController = BitmapController()

    companion object {
        val audioDataArray = mutableListOf<AudioModel>()
    }

    fun getAudiosFromDevice(context: Context) : MutableList<AudioModel> {
        //Define the URL schema to search files
        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        //Find the content based on Projection (second argument)
        //selection "${MediaStore.Audio.Media.TITLE} = 'emicida semente with photyo'" return only this music, can be useful
        //to search action
        //selection "${MediaStore.Audio.Media.TITLE} LIKE '%AUD%'" return all with has "AUD" in title
        //val args = arrayOf("%AUD%")
        val songCursor = context.contentResolver.query(
            songUri,
            arrayOf(MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA),
            null         /*"${MediaStore.Audio.Media.TITLE} LIKE ?"*/,
            null      /*args*/,
            null        /*"${MediaStore.Audio.Media.TITLE} DESC"*/
        )

        //If cursor and cursor is not empty
        if (songCursor != null && songCursor.moveToFirst()) {
            //prepare to get audio title column
            val songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            //prepare to get audio data column (File Uri)
            val songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)

            do {
                //get data from every element (one by one)
                // get title
                val currentTitle = songCursor.getString(songTitle)
                //get location uri
                val currentLocation = songCursor.getString(songLocation)
                //get album img
                val currentArtUri = bitmapController.getAlbumImage(currentLocation)

                //add to list of audios
                audioDataArray.add(
                    AudioModel(
                        currentTitle,
                        currentLocation,
                        currentArtUri
                    )
                )
                //repeat while has more rows in db
            } while (songCursor.moveToNext())
        }

        //detach database connection
        songCursor?.close()
        return audioDataArray
    }

    fun playFromUri(context: Context, audioUri: Uri){
        val mediaPlayer = MediaPlayer.create(context, audioUri)
        mediaPlayer.start()
    }
}