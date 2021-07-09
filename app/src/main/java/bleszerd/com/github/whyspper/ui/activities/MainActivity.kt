package bleszerd.com.github.whyspper.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import bleszerd.com.github.whyspper.R
import bleszerd.com.github.whyspper.adapters.AudioAdapter
import bleszerd.com.github.whyspper.controllers.AudioController
import bleszerd.com.github.whyspper.models.AudioModel
import bleszerd.com.github.whyspper.ui.fragments.AudioListFragment
import bleszerd.com.github.whyspper.ui.fragments.AudioListFragment.OnAudioSelected
import bleszerd.com.github.whyspper.ui.fragments.BottomAudioActionFragment
import bleszerd.com.github.whyspper.ui.fragments.HeaderTopFragment
import bleszerd.com.github.whyspper.ui.fragments.HomePlayerFragment


class MainActivity : AppCompatActivity(), OnAudioSelected {
    val audioController = AudioController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        handleExternalStoragePermission()
//        val deviceAudios = mutableListOf<AudioModel>()

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
                    R.id.bottomActionSectionHost,
                    BottomAudioActionFragment.newInstance()
                )
                .commit()

            if(isExternalStoragePermissionGranted()){
                supportFragmentManager
                    .beginTransaction()
                    .add(
                        R.id.mainContentCenterHost,
                        AudioListFragment.newInstance(AudioController.audioDataArray)
                    )
                    .commit()
            }
        }
    }

    fun handleExternalStoragePermission() {
        if (!isExternalStoragePermissionGranted()) {
            val intent = Intent(this, RequestPermissionsActivity::class.java)
            permissionResult.launch(intent)
        }
    }

    private fun isExternalStoragePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onAudioSelected(audio: AudioModel) {
        audioController.playFromUri(applicationContext, Uri.parse(audio.location))
    }

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val extraValue = result.data?.extras?.get("permissionResult")
                if(extraValue == PackageManager.PERMISSION_GRANTED){
                    audioController.getAudiosFromDevice(this)

                    supportFragmentManager
                        .beginTransaction()
                        .add(
                            R.id.mainContentCenterHost,
                            AudioListFragment.newInstance(AudioController.audioDataArray)
                        )
                        .commit()
                }
            }
        }
}