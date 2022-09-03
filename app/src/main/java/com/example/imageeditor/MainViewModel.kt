package com.example.imageeditor

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {

    var aspectRation: String = "1";
    var fileInfo: MutableLiveData<String> = MutableLiveData("File Info: ")

    var mainListener: MainListener? = null

    fun onImagePicker(view: View) {
        mainListener?.onImagePickerDialog()
    }

    fun xFlip(view: View) {
        mainListener?.xFlip()
    }

    fun yFlip(view: View) {
        mainListener?.yFlip()
    }

    fun crop(view: View, x: Int, y: Int) {
        mainListener?.crop(x, y)
    }

}