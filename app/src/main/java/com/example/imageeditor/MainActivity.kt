package com.example.imageeditor

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.imageeditor.databinding.ActivityMainBinding
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), MainListener {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.mainListener = this
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        initialization(binding.root)

    }

    private fun initialization(root: View) {

    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onImagePickerDialog() {
        if (checkPermissionForStorage()) {
            val intent: Intent = Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT)

            startActivityForResult(Intent.createChooser(intent, "Select an image"), 123)
        }
    }

    override fun xFlip() {
        if (binding.imgImage.scaleX == 1.0f) {
            binding.imgImage.scaleX = -1.0f
        } else {
            binding.imgImage.scaleX = 1.0f
        }
    }

    override fun yFlip() {
        if (binding.imgImage.scaleY == 1.0f) {
            binding.imgImage.scaleY = -1.0f
        } else {
            binding.imgImage.scaleY = 1.0f
        }
    }

    override fun crop(x: Int, y: Int) {
        if (x == 1 && y == 1) {
            binding.imgImage.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissionForStorage(): Boolean {
        return if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
            false
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            onImagePickerDialog()
        }
        if (requestCode == 123 && resultCode == RESULT_OK) {
            val selectedFile: Uri = data?.data!!
            binding.imgImage.setImageURI(selectedFile)
            // gathering information and showing
            val options: BitmapFactory.Options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(
                this.contentResolver.openInputStream(selectedFile),
                null,
                options
            )
            binding.viewModel?.fileInfo?.value = "File Info: \n Filepath: ${selectedFile.path} \n Resolution: ${options.outHeight}x${options.outWidth}"
            Toast.makeText(this, "Selected image", Toast.LENGTH_SHORT).show()
        }
    }
}