package bleszerd.com.github.whyspper.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.models.AudioModel
import bleszerd.com.github.whyspper.ui.fragments.MusicListFragment


class MainActivity : AppCompatActivity() {
    private val MY_PERMISSION_REQUEST = 1

    private lateinit var audioArray: MutableList<AudioModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkForPermissions()
        populateAudioArray()

        if (savedInstanceState == null) {
            //attach fragment to frameRoot (on main activity layout file) passing a audioArray by newInstance companion method
            supportFragmentManager
                .beginTransaction()
                .add(R.id.frameRoot, MusicListFragment.newInstance(audioArray))
                .commit()
        }
    }

    private fun checkForPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSION_REQUEST
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSION_REQUEST
                )
            }
        }
    }

    private fun populateAudioArray() {
        getMusic()
    }

    private fun getMusic() {
        //Initialize array of audioModel
        audioArray = arrayListOf()

        //Define the URL schema to search files
        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        //Find the content based on Projection (second argument)
        //selection "${MediaStore.Audio.Media.TITLE} = 'emicida semente with photyo'" return only this music, can be useful
        //to search action
        //selection "${MediaStore.Audio.Media.TITLE} LIKE '%AUD%'" return all with has "AUD" in title
        //val args = arrayOf("%AUD%")
        val songCursor = contentResolver.query(
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
                audioArray.add(AudioModel(currentTitle, currentLocation, currentArtUri))
                //repeat while has more rows in db
            } while (songCursor.moveToNext())
        }

        //detach database connection
        songCursor?.close()
    }

    private fun getAlbumImage(path: String): Bitmap? {
        //create MediaMetadataRetriever instance
        val mmr = MediaMetadataRetriever()

        //pointer the MediaMetadataRetriever to a specific path
        mmr.setDataSource(path)

        //get a album picture from metadata
        val data = mmr.embeddedPicture

        //can be used for extract a lot of others info's
        //val bitrate = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)

        //return the album cover img if exists, else return null
        //BitmapFactory.decodeByteArray get the DATA and parse into bitmap
        return if (data != null) BitmapFactory.decodeByteArray(data, 0, data.size) else null
    }

    //callback from requestPermissions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //verify permission
        when (requestCode) {
            MY_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if permission is granted...
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                    }
                    //if permission is denied...
                    else {
                        Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show()
                    }
                    return
                }
            }
        }
    }
}