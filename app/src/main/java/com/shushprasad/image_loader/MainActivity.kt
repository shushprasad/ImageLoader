package com.shushprasad.image_loader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shushprasad.image_loader.adapter.ImageAdapter
import com.shushprasad.image_loader.utils.GridSpacingItemDecoration
import kotlinx.coroutines.*
import org.json.JSONArray
import java.io.IOException
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var imageAdapter: ImageAdapter
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        val itemDecoration = GridSpacingItemDecoration(this, 3, spacingInPixels, true)
        recyclerView.addItemDecoration(itemDecoration)

        val imageLoader = ImageLoader(this)

        // Make API call to fetch image URLs
        job = CoroutineScope(Dispatchers.IO).launch {
            val imageUrls = fetchImageUrls()
            withContext(Dispatchers.Main) {
                imageAdapter = ImageAdapter(imageUrls, imageLoader)
                recyclerView.adapter = imageAdapter
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel() // Cancel the ongoing coroutine job when the activity is destroyed
    }

    private suspend fun fetchImageUrls(): List<String> {
        val url = "https://acharyaprashant.org/api/v2/content/misc/media-coverages?limit=100"
        return try {
            val response = URL(url).readText()

            val jsonArray = JSONArray(response)
            val imageUrls = mutableListOf<String>()

            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                val thumbnailObject = jsonObject.getJSONObject("thumbnail")
                val domain = thumbnailObject.getString("domain")
                val basePath = thumbnailObject.getString("basePath")
                val key = thumbnailObject.getString("key")

                val imageUrl = "$domain/$basePath/0/$key"
                imageUrls.add(imageUrl)
            }

            imageUrls
        } catch (e: IOException) {
            e.printStackTrace()
            emptyList()
        }
    }
}
