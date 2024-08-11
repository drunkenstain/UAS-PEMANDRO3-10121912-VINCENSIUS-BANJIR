package com.example.banjir2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewLatest: RecyclerView
    private lateinit var adapterLatest: NewsAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var olderNewsLayout: LinearLayout
    private lateinit var searchEditText: EditText
    private lateinit var searchButton: Button
    private val newsListLatest = NewsRepository.newsList
    private val newsListOlder = NewsRepository.newsListOlder

    @RequiresApi(Build.VERSION_CODES.O)
    private fun notifyNewNews(news: News) {
        val notificationHelper = NotificationHelper(this)
        notificationHelper.sendNotification(news)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        setThemeBasedOnPreferences()

        setContentView(R.layout.activity_main)

        recyclerViewLatest = findViewById(R.id.recyclerViewLatest)
        olderNewsLayout = findViewById(R.id.olderNewsLayout)
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)

        // Retrieve text size and font preferences
        val textSize = sharedPreferences.getInt("textSize", 16)
        val font = sharedPreferences.getString("font", "Default") ?: "Default"

        recyclerViewLatest.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapterLatest = NewsAdapter(newsListLatest, this, textSize, font, false)
        recyclerViewLatest.adapter = adapterLatest

        populateOlderNews()

        searchButton.setOnClickListener {
            val query = searchEditText.text.toString()
            // Implement search functionality here
        }

        findViewById<Button>(R.id.button_favorites).setOnClickListener {
            val intent = Intent(this, FavoriteActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_settings).setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_feedback).setOnClickListener {
            val intent = Intent(this, FeedbackActivity::class.java)
            startActivity(intent)
        }
    }

    private fun populateOlderNews() {
        olderNewsLayout.removeAllViews()

        for (i in newsListOlder.indices step 2) {
            val rowLayout = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
                weightSum = 2f
                setPadding(8, 8, 8, 8)
            }

            val news1 = newsListOlder[i]
            rowLayout.addView(createNewsItemView(news1))

            if (i + 1 < newsListOlder.size) {
                val news2 = newsListOlder[i + 1]
                rowLayout.addView(createNewsItemView(news2))
            } else {
                rowLayout.addView(createEmptyView())
            }

            olderNewsLayout.addView(rowLayout)
        }
    }

    private fun createNewsItemView(news: News): View {
        val newsLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            setPadding(4, 4, 4, 4)
            setOnClickListener {
                // Handle click event for the news item
                val intent = Intent(this@MainActivity, NewsDetailActivity::class.java).apply {
                    putExtra("newsTitle", news.title)
                    putExtra("newsDescription", news.description)
                    putExtra("newsImageResId", news.imageResId)
                }
                startActivity(intent)
            }
        }

        val imageView = ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150.dpToPx())
            scaleType = ImageView.ScaleType.CENTER_CROP
            setImageResource(news.imageResId)
        }

        val titleTextView = TextView(this).apply {
            text = news.title
            textSize = sharedPreferences.getInt("textSize", 16).toFloat()
            setPadding(0, 8, 0, 0)
            typeface = getTypeface(sharedPreferences.getString("font", "Default"))
        }

        newsLayout.addView(imageView)
        newsLayout.addView(titleTextView)

        return newsLayout
    }

    private fun createEmptyView(): View {
        return View(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
    }

    private fun setThemeBasedOnPreferences() {
        val theme = sharedPreferences.getString("theme", "Light")
        val themeId = when (theme) {
            "Dark" -> R.style.AppTheme_Dark
            else -> R.style.AppTheme_Light
        }
        setTheme(themeId)
    }

    private fun getTypeface(font: String?): Typeface {
        return when (font) {
            "Custom" -> Typeface.create("sans-serif-condensed", Typeface.NORMAL)
            else -> Typeface.DEFAULT
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }

    private fun toggleFavorite(news: News) {
        val isFavorite = sharedPreferences.getBoolean(news.title, false)
        with(sharedPreferences.edit()) {
            putBoolean(news.title, !isFavorite)
            apply()
        }
        adapterLatest.notifyDataSetChanged()
    }
}
