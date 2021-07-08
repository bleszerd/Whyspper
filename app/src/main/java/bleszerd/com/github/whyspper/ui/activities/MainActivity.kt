package bleszerd.com.github.whyspper.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioAttributes
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
import javax.xml.transform.Source


class MainActivity : AppCompatActivity() {
    private val MY_PERMISSION_REQUEST = 1

    private lateinit var arrayList: MutableList<AudioModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkForPermissions()
        doStuff()

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.frameRoot, MusicListFragment.newInstance(arrayList))
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

    private fun doStuff() {
        getMusic()
    }

    private fun getMusic() {
        //Initialize array of audioModel
        arrayList = arrayListOf()

        //Define the URL schema to search files
        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        //Find the content based on Projection (second argument)

        //selection "${MediaStore.Audio.Media.TITLE} = 'emicida semente with photyo'" return only this music, can be usefull
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


        if (songCursor != null && songCursor.moveToFirst()) {
            val songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)

            do {
                val currentTitle = songCursor.getString(songTitle)
                println(currentTitle)

                val currentLocation = songCursor.getString(songLocation)
                val currentArt = getAlbumImage(currentLocation)
                arrayList.add(AudioModel(currentTitle, currentLocation, currentArt))
            } while (songCursor.moveToNext())
        }

        songCursor?.close()
    }

    private fun getAlbumImage(path: String): Bitmap? {
        val mmr = MediaMetadataRetriever()
        mmr.setDataSource(path)
        val data = mmr.embeddedPicture
//        val title = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)
        return if (data != null) BitmapFactory.decodeByteArray(data, 0, data.size) else null
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            MY_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                        doStuff()
                    } else {
                        Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show()
                    }
                    return
                }
            }
        }
    }
}