package com.example.sockettest

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import android.view.Surface
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class GalleryActivity : AppCompatActivity() {

    lateinit var images: List<File>
    lateinit var txtNoPhotos: TextView
    lateinit var galleryAdapter: GalleryImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
        askForStoragePermission()

        var recyclerView: RecyclerView = findViewById(R.id.galleryRecView)
        txtNoPhotos = findViewById(R.id.txtNoPhotos)
        images = ImageUtils.getImagesFromFolder(this@GalleryActivity, "BoatApp")
        val window: Window = this@GalleryActivity.window
        window.navigationBarColor = ContextCompat.getColor(this@GalleryActivity, R.color.black)

        galleryAdapter = GalleryImageAdapter(this, images)
        recyclerView.adapter = galleryAdapter

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.layoutManager = GridLayoutManager(this, 3)
        } else {
            recyclerView.layoutManager = GridLayoutManager(this, 6)
        }

        if (images.isEmpty()) {
            txtNoPhotos.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        images = ImageUtils.getImagesFromFolder(this@GalleryActivity, "BoatApp")
        galleryAdapter.notifyDataSetChanged()
        if (images.isEmpty()) {
            txtNoPhotos.visibility = View.VISIBLE
        } else {
            txtNoPhotos.visibility = View.GONE
        }
    }

    private val writeStoragePermissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions -> }

    private fun askForStoragePermission(): Boolean =
        if (hasPermissions(
                this@GalleryActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            true
        } else {
            writeStoragePermissionResult.launch(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                )
            )
            false
        }

    private fun hasPermissions(context: Context, vararg permissions: String): Boolean =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
}