package com.wedoapps.barcodescanner.Ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wedoapps.barcodescanner.Utils.Constants
import com.wedoapps.barcodescanner.databinding.ActivityChoiceBinding

class ChoiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChoiceBinding
    private val permission = listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar.customToolbar
        setSupportActionBar(toolbar)

        checkPermission(100)

        binding.toolbar.ivBack.visibility = View.GONE
        binding.toolbar.ivAddUsers.visibility = View.GONE

        binding.cardBill.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.cardAddUpdateStock.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        binding.cardAddUser.setOnClickListener {
            startActivity(Intent(this, UserListActivity::class.java))
        }
    }

    private fun checkPermission(requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission[0]
            ) == PackageManager.PERMISSION_DENIED && ContextCompat.checkSelfPermission(
                this,
                permission[1]
            ) == PackageManager.PERMISSION_DENIED && ContextCompat.checkSelfPermission(
                this,
                permission[2]
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // Requesting the permission
            ActivityCompat.requestPermissions(
                this,
                permission.toTypedArray(), requestCode
            )
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.i(Constants.TAG, "Permission: Granted")
        } else {
            Log.i(Constants.TAG, "Permission: Denied")
        }
    }

}