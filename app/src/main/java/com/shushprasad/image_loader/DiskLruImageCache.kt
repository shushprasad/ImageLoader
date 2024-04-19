package com.shushprasad.image_loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.OutputStream
import java.security.MessageDigest

class DiskLruImageCache(context: Context) {
    private val cacheDir: File = context.cacheDir

    fun putBitmap(key: String, bitmap: Bitmap) {
        val file = File(cacheDir, hashKeyForDisk(key))
        var out: OutputStream? = null
        try {
            out = file.outputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        } finally {
            out?.close()
        }
    }

    fun getBitmap(key: String): Bitmap? {
        val file = File(cacheDir, hashKeyForDisk(key))
        return if (file.exists()) {
            BitmapFactory.decodeFile(file.absolutePath)
        } else {
            null
        }
    }

    private fun hashKeyForDisk(key: String): String {
        return MessageDigest.getInstance("MD5").digest(key.toByteArray()).joinToString("") {
            "%02x".format(it)
        }
    }
}
