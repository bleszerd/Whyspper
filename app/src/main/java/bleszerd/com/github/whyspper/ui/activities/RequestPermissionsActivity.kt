package bleszerd.com.github.whyspper.ui.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import bleszerd.com.github.whyspper.R


class RequestPermissionsActivity : AppCompatActivity() {
    private val EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1
    private val requiredPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.request_permissions_activity)

        findViewById<Button>(R.id.buttonPermission).setOnClickListener {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            requiredPermissions,
            EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE -> {
                if(grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED){
                    finish()
                } else {
                    val helpText = findViewById<TextView>(R.id.helpText)
                    val centerText = findViewById<TextView>(R.id.centerDescription)
                    centerText.text = "Você negou o acesso aos dados!"
                    helpText.visibility = View.VISIBLE
                }
            }
        }
    }
}