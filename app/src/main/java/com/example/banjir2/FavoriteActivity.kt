package com.example.banjir2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoriteActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapter
    private val favoriteNewsList: MutableList<News> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setThemeBasedOnPreferences() // Optional: Apply theme based on preferences
        setContentView(R.layout.activity_favorite)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadFavorites()

        // Use default values for textSize, font, and isFavoriteSection
        val defaultTextSize = 16 // Replace with actual default text size if needed
        val defaultFont = "Default" // Replace with actual default font if needed
        val isFavoriteSection = true // This is specific to the favorite section

        adapter = NewsAdapter(favoriteNewsList, this, defaultTextSize, defaultFont, isFavoriteSection)
        recyclerView.adapter = adapter
    }

    private fun loadFavorites() {
        val sharedPreferences = getSharedPreferences("Bookmarks", MODE_PRIVATE)
        for (news in NewsRepository.newsList) {
            if (sharedPreferences.getBoolean(news.title, false)) {
                favoriteNewsList.add(news)
            }
        }
    }

    private fun setThemeBasedOnPreferences() {
        val sharedPreferences = getSharedPreferences("AppSettings", MODE_PRIVATE)
        val theme = sharedPreferences.getString("theme", "Light")
        val themeId = when (theme) {
            "Dark" -> R.style.AppTheme_Dark
            else -> R.style.AppTheme_Light
        }
        setTheme(themeId)
    }
}
