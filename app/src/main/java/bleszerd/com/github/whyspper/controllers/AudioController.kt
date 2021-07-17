package bleszerd.com.github.whyspper.controllers

import android.content.Context
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.listeners.AudioStateListener
import bleszerd.com.github.whyspper.models.AudioModel

class AudioController {
    companion object {
        private var currentPlayer: MediaPlayer? = null
        private lateinit var currentAudio: AudioModel
        val audioDataArray = mutableListOf<AudioModel>()
        lateinit var listener: AudioStateListener

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
            val currentAudioArray = audioDataArray.filter {
                it.location == audioUri.toString()
            }

            this.currentAudio = currentAudioArray[0]

            if (currentPlayer != null) {
                currentPlayer?.stop()
                listener.onAudioStop(context, currentAudio)

                currentPlayer?.release()
                listener.onAudioRelease(context, currentAudio)

                currentPlayer = MediaPlayer.create(context, audioUri)
                listener.onAudioChange(context, currentAudio)

                currentPlayer?.start()
                listener.onAudioStart(context, currentAudio)
            } else {
                currentPlayer = MediaPlayer.create(context, audioUri)
                listener.onAudioChange(context, currentAudio)

                currentPlayer?.start()
                listener.onAudioStart(context, currentAudio)
            }

            currentPlayer?.setOnCompletionListener {
                listener.onAudioEnd()
            }
        }

        fun togglePlayPauseState() {
            if (currentPlayer?.isPlaying!!) {
                currentPlayer?.pause()
                listener.onAudioPause(currentAudio)
            } else {
                currentPlayer?.start()
                listener.onAudioResume(currentAudio)
            }
        }

        fun toggleFavoriteState(likeImageView: ImageView) {
            //change favorite state on click
            currentAudio.favorite = !currentAudio.favorite

            val imgResource =
                if (currentAudio.favorite) R.drawable.ic_favorite else R.drawable.ic_favorite_empty

            //update button resource
            likeImageView.setImageResource(imgResource)
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