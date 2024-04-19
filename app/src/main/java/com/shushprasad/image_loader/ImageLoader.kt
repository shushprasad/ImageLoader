package com.shushprasad.image_loader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import java.io.IOException
import java.net.URL


class ImageLoader(private val context: Context) {
    private val memoryCache = LruCache<String, Bitmap>(cacheSize)
    private val diskCache = DiskLruImageCache(context)

    suspend fun loadImage(url: String): Bitmap? {
        // Check memory cache
        val bitmapFromMemory = memoryCache.get(url)
        if (bitmapFromMemory != null) {
            return bitmapFromMemory
        }

        // Check disk cache
        val bitmapFromDisk = diskCache.getBitmap(url)
        if (bitmapFromDisk != null) {
            memoryCache.put(url, bitmapFromDisk)
            return bitmapFromDisk
        }

        // Fetch image from network
        val bitmap = fetchImageFromNetwork(url)

        // Cache image
        if (bitmap != null) {
            memoryCache.put(url, bitmap)
            diskCache.putBitmap(url, bitmap)
        }

        return bitmap
    }

    private suspend fun fetchImageFromNetwork(url: String): Bitmap? {
        return try {
            val response = URL(url).readBytes()
            BitmapFactory.decodeByteArray(response, 0, response.size)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        private const val cacheSize = 10 * 1024 * 1024 // 10 MiB
    }
}
