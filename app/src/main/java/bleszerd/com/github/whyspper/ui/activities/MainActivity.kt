package bleszerd.com.github.whyspper.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.adapters.MusicAdapter
import bleszerd.com.github.whyspper.models.AudioModel
import bleszerd.com.github.whyspper.ui.fragments.MusicListFragment


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
        } else {
            doStuff()
        }
    }

    private fun doStuff() {
        getMusic()
        println(arrayList)
    }

    private fun getMusic() {
        arrayList = arrayListOf()
        val songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val songCursor = contentResolver.query(songUri, null, null, null, null)

        if (songCursor != null && songCursor.moveToFirst()) {
            val songTitle = songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val songLocation = songCursor.getColumnIndex(MediaStore.Audio.Media.DATA)

            do {
                val currentTitle = songCursor.getString(songTitle)
                val currentLocation = songCursor.getString(songLocation)
                arrayList.add(AudioModel(currentTitle, currentLocation))
            } while (songCursor.moveToNext())
        }

        songCursor?.close()
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