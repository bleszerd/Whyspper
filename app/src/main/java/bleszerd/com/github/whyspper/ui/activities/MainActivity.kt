package bleszerd.com.github.whyspper.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.models.AudioModel
import bleszerd.com.github.whyspper.ui.fragments.AudioListFragment
import bleszerd.com.github.whyspper.ui.fragments.BottomAudioActionFragment
import bleszerd.com.github.whyspper.ui.fragments.HeaderTopFragment
import bleszerd.com.github.whyspper.ui.fragments.HomePlayerFragment


class MainActivity : AppCompatActivity() {
    val audioDataArray = mutableListOf<AudioModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handleExternalStoragePermission()
//        populateAudioArray()
        getAudioFromDevice(this)

        if (savedInstanceState == null) {
            //attach fragment to frameRoot (on main activity layout file) passing a audioArray by newInstance companion method
            supportFragmentManager
                .beginTransaction()
                .add(R.id.frameRoot, HomePlayerFragment.newInstance())
                .commit()

            supportFragmentManager
                .beginTransaction()
                .add(R.id.headerTopSectionHost, HeaderTopFragment.newInstance())
                .commit()

            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.mainContentCenterHost,
                    AudioListFragment.newInstance(audioDataArray)
                )
                .commit()

            supportFragmentManager
                .beginTransaction()
                .add(
                    R.id.bottomActionSectionHost,
                    BottomAudioActionFragment.newInstance()
                )
                .commit()
        }
    }

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

    fun getAudioFromDevice(context: Context) {
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
                val currentArtUri = getAlbumImage(currentLocation)

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
    }

    fun handleExternalStoragePermission() {
        if (!isExternalStoragePermissionGranted()) {
            val intent = Intent(this, RequestPermissionsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isExternalStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun populateAudioArray() {
//        AudioController.getAudioFromDevice(this)
    }
}