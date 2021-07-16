package bleszerd.com.github.whyspper.controllers

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageButton
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.models.AudioModel

class AudioController {
    companion object {
        private var currentPlayer: MediaPlayer? = null
        private lateinit var currentAudio: AudioModel
        val audioDataArray = mutableListOf<AudioModel>()

        fun getAudiosFromDevice(context: Context): MutableList<AudioModel> {
            //Define the URL schema to search files
            val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

            //Find the content based on Projection (second argument)
            //selection "${MediaStore.Audio.Media.TITLE} = 'emicida semente with photyo'" return only this music, can be useful
            //to search action
            //selection "${MediaStore.Audio.Media.TITLE} LIKE '%AUD%'" return all with has "AUD" in title
            //val args = arrayOf("%AUD%")
            val songCursor = context.contentResolver.query(
                songUri,
                arrayOf(
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Artists.ARTIST
                ),
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
                //prepare to get audio artist column
                val songArtist = songCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)

                do {
                    //get data from every element (one by one)
                    // get title
                    val currentTitle = songCursor.getString(songTitle)
                    //get location uri
                    val currentLocation = songCursor.getString(songLocation)

                    //validate audio file existence
                    if (!validateAudioExistence(currentLocation)) continue

                    //get album img
                    val currentArtBitmap = BitmapController.getAlbumImage(context, currentLocation)

                    //get artist
                    val currentArtist = songCursor.getString(songArtist)
                    //check if audio is favorite
                    val currentFavorite = false

                    //add to list of audios
                    audioDataArray.add(
                        AudioModel(
                            currentTitle,
                            currentArtist,
                            currentFavorite,
                            currentLocation,
                            currentArtBitmap,
                            "${currentTitle}:${currentLocation}"
                        )
                    )
                    //repeat while has more rows in db
                } while (songCursor.moveToNext())
            }

            //detach database connection
            songCursor?.close()
            return audioDataArray
        }

        fun playFromUri(context: Context, audioUri: Uri) {

            if (currentPlayer != null) {
                currentPlayer?.stop()
                currentPlayer?.release()
                currentPlayer = MediaPlayer.create(context, audioUri)
                currentPlayer?.start()
            } else {
                currentPlayer = MediaPlayer.create(context, audioUri)
                currentPlayer?.start()
            }

            val audio = audioDataArray.filter {
                it.location == audioUri.toString()
            }

            currentAudio = audio[0]
        }

        fun pauseOrPlay(button: ImageButton) {
            if (currentPlayer?.isPlaying!!) {
                currentPlayer?.pause()
                button.setImageResource(R.drawable.ic_play_btn)
            } else {
                currentPlayer?.start()
                button.setImageResource(R.drawable.ic_pause_btn)
            }
        }

        fun handleChangeFavoriteAction(button: ImageButton) {
            //change favorite state on click
            currentAudio.favorite = !currentAudio.favorite

            val buttonResource =
                if (currentAudio.favorite) R.drawable.ic_favorite else R.drawable.ic_favorite_empty

            //update button resource
            button.setImageResource(buttonResource)
        }

        private fun validateAudioExistence(audioPath: String): Boolean {
            val mediaRetriever = MediaMetadataRetriever()

            return try {
                //pointer the MediaMetadataRetriever to a specific path
                mediaRetriever.setDataSource(audioPath)
                true
            } catch (e: Exception) {
                false
            }
        }
    }


}