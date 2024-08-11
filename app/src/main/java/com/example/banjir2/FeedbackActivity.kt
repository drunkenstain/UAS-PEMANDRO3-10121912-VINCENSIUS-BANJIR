package com.example.banjir2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.google.firebase.firestore.FirebaseFirestore

class FeedbackActivity : AppCompatActivity() {

    private lateinit var editTextFeedback: EditText
    private lateinit var buttonSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        editTextFeedback = findViewById(R.id.editTextFeedback)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        buttonSubmit.setOnClickListener {
            val feedback = editTextFeedback.text.toString()
            if (feedback.isNotEmpty()) {
                sendFeedbackEmail(feedback)
            }
        }
    }

    private fun sendFeedbackEmail(feedback: String) {
        // Buat intent untuk mengirim email
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "message/rfc822"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("vincennnn@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Feedback dari Aplikasi")
            putExtra(Intent.EXTRA_TEXT, feedback)
        }

        try {
            startActivity(Intent.createChooser(intent, "Kirim feedback dengan:"))
        } catch (e: android.content.ActivityNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun saveFeedbackToFirestore(feedback: String) {
        val firestore = FirebaseFirestore.getInstance()
        val feedbackData = hashMapOf(
            "feedback" to feedback,
            "timestamp" to System.currentTimeMillis()
        )
        firestore.collection("feedbacks")
            .add(feedbackData)
            .addOnSuccessListener {
                // Berhasil menyimpan feedback
            }
            .addOnFailureListener {
                // Gagal menyimpan feedback
            }
    }
}
