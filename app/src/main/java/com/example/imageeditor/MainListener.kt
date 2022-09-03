package com.example.imageeditor

interface MainListener {
    fun onImagePickerDialog()
    fun xFlip()
    fun yFlip()
    fun crop(x: Int, y: Int)
}