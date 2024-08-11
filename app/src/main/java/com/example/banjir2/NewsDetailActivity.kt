package com.example.banjir2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NewsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        // Ambil data dari Intent
        val title = intent.getStringExtra("newsTitle") ?: ""
        val description = intent.getStringExtra("newsDescription") ?: ""
        val imageResId = intent.getIntExtra("newsImageResId", 0)

        // Inisialisasi Views
        val titleView: TextView = findViewById(R.id.newsTitle)
        val descriptionView: TextView = findViewById(R.id.newsDescription)
        val imageView: ImageView = findViewById(R.id.newsImageView)
        val shareButton: Button = findViewById(R.id.shareButton)
        val commentButton: Button = findViewById(R.id.commentButton)

        // Set data ke Views
        titleView.text = title
        descriptionView.text = description
        imageView.setImageResource(imageResId)

        // Atur aksi tombol berbagi
        shareButton.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "$title\n$description")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Bagikan berita ke:"))
        }

        // Atur aksi tombol komentar
        commentButton.setOnClickListener {
            val intent = Intent(this, CommentActivity::class.java).apply {
                // Pass title as extra to CommentActivity
                putExtra("newsTitle", title)
            }
            startActivity(intent)
        }
    }
}
