package com.example.banjir2

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CommentActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var commentEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var sharedPreferences: SharedPreferences

    private val commentList: MutableList<Comment> = mutableListOf()
    private var newsTitle: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comment)

        sharedPreferences = getSharedPreferences("Comments", MODE_PRIVATE)
        loadComments()

        recyclerView = findViewById(R.id.recyclerView)
        commentEditText = findViewById(R.id.commentEditText)
        submitButton = findViewById(R.id.submitButton)

        // Get data from Intent
        newsTitle = intent.getStringExtra("newsTitle")

        recyclerView.layoutManager = LinearLayoutManager(this)
        commentAdapter = CommentAdapter(commentList)
        recyclerView.adapter = commentAdapter

        submitButton.setOnClickListener {
            postComment()
        }
    }

    private fun loadComments() {
        val json = sharedPreferences.getString("comment_list", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Comment>>() {}.type
            commentList.addAll(Gson().fromJson(json, type))
        }
    }

    private fun postComment() {
        val content = commentEditText.text.toString().trim()
        if (content.isNotEmpty() && newsTitle != null) {
            val comment = Comment(username = "User", content = content)
            commentList.add(comment)
            saveComments()
            commentEditText.text.clear()
            commentAdapter.notifyDataSetChanged()
        }
    }

    private fun saveComments() {
        val json = Gson().toJson(commentList)
        sharedPreferences.edit().putString("comment_list", json).apply()
    }
}

