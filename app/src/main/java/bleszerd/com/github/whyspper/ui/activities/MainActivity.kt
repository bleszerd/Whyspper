package bleszerd.com.github.whyspper.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.adapters.AudioAdapter
import bleszerd.com.github.whyspper.controllers.AudioController
import bleszerd.com.github.whyspper.models.AudioModel
import bleszerd.com.github.whyspper.ui.fragments.*
import bleszerd.com.github.whyspper.ui.fragments.AudioListFragment.OnAudioSelected

class MainActivity : AppCompatActivity(), OnAudioSelected {
    lateinit var playPauseButton: ImageButton

    //create a result contract with request permission activity
    private val permissionResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                val extraValue = result.data?.extras?.get("permissionResult")
                //if permission is granted initialize MainContentHost
                if (extraValue == PackageManager.PERMISSION_GRANTED) {
                    initializeMainContent()
                }
            }
        }

    private val audioController = AudioController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handleExternalStoragePermission()
        initializeFragments(savedInstanceState)
    }

    private fun initializeFragments(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            //attach fragment to frameRoot (on main activity layout file) passing a audioArray by newInstance companion method
            initializeFrameRoot()
            initializeTopHeaderSection()
            initializeBottomActionSection()
            initializeBottomAudioActionContent()

            if (isExternalStoragePermissionGranted()) {
                initializeMainContent()
            }
        }
    }

    //populate frame layout bottomActionSectionHost
    private fun initializeBottomActionSection() {
        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.bottomActionSectionHost, BottomAudioActionFragment.newInstance()
            )
            .commit()
    }

    //populate frame layout headerTopSectionHost
    private fun initializeTopHeaderSection() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.headerTopSectionHost, HeaderTopFragment.newInstance())
            .commit()
    }

    //populate frame layout frameRoot
    private fun initializeFrameRoot() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.frameRoot, HomePlayerFragment.newInstance())
            .commit()
    }

    private fun initializeBottomAudioActionContent() {
        supportFragmentManager
            .beginTransaction()
            .add(R.id.bottomAudioActionContent, BottomAudioActionContentFragment.newInstance())
            .commit()
    }


    private fun initializeMainContent() {
        //fetch audios from device
        audioController.getAudiosFromDevice(this)

        //populate frame layout mainContentCenterHost
        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.mainContentCenterHost,
                AudioListFragment.newInstance(AudioController.audioDataArray)
            )
            .commit()
    }

    //if permission is denied or not requested launch a new activity to solve this
    private fun handleExternalStoragePermission() {
        if (!isExternalStoragePermissionGranted()) {
            val intent = Intent(this, RequestPermissionsActivity::class.java)
            permissionResult.launch(intent)
        }
    }

    //verify if external permission is granted
    private fun isExternalStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    //on album tap action
    override fun onAudioSelected(audio: AudioModel) {
        //play content from audioUri
        audioController.playFromUri(applicationContext, Uri.parse(audio.location))

        //update bottom content to current music
        val imageCover = findViewById<ImageView>(R.id.albumImageCover)
        imageCover.setImageBitmap(audio.art)

        //update image button resource
        val playPauseBtn = findViewById<ImageButton>(R.id.playPauseActionButton)
        playPauseBtn.setImageResource(R.drawable.ic_pause_btn)
    }
}