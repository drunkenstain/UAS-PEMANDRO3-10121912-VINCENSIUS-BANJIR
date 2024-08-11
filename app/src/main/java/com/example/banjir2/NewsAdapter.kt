package com.example.banjir2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NewsAdapter(
    private val newsList: List<News>,
    private val context: Context,
    private val textSize: Int,
    private val font: String,
    private val isFavoriteSection: Boolean
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("Bookmarks", Context.MODE_PRIVATE)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList[position]

        // Bind the image and title
        holder.titleView.text = news.title
        holder.titleView.textSize = textSize.toFloat()
        holder.titleView.typeface = Typeface.create(font, Typeface.NORMAL)
        holder.imageView.setImageResource(news.imageResId)

        // Check if the news item is bookmarked
        val isBookmarked = sharedPreferences.getBoolean(news.title, false)
        holder.bookmarkButton.setImageResource(
            if (isBookmarked) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border
        )

        // Handle bookmark button click
        holder.bookmarkButton.setOnClickListener {
            val editor = sharedPreferences.edit()
            if (isBookmarked) {
                // Remove bookmark
                editor.remove(news.title).apply()
                holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark_border)
            } else {
                // Add bookmark
                editor.putBoolean(news.title, true).apply()
                holder.bookmarkButton.setImageResource(R.drawable.ic_bookmark)
            }
        }

        // Handle click events for news items
        holder.itemView.setOnClickListener {
            val intent = Intent(context, NewsDetailActivity::class.java).apply {
                putExtra("newsTitle", news.title)
                putExtra("newsDescription", news.description)
                putExtra("newsImageResId", news.imageResId)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    class NewsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val titleView: TextView = view.findViewById(R.id.titleView)
        val bookmarkButton: ImageButton = view.findViewById(R.id.bookmarkButton)
        val descriptionView: TextView = view.findViewById(R.id.descriptionView)
    }
}
