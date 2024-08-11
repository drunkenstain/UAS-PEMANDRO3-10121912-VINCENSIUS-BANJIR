package com.example.banjir2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.SeekBar
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        sharedPreferences = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)

        val textSizeSeekBar: SeekBar = findViewById(R.id.textSizeSeekBar)
        val textSizeValue: TextView = findViewById(R.id.textSizeValue)
        val applyButton: Button = findViewById(R.id.applyButton)
        val fontSpinner: Spinner = findViewById(R.id.fontSpinner)

        // Set initial values from SharedPreferences
        val savedTextSize = sharedPreferences.getInt("textSize", 16)
        textSizeSeekBar.progress = savedTextSize - 10
        textSizeValue.text = "$savedTextSize"

        val savedFont = sharedPreferences.getString("font", "Default")
        val fontOptions = arrayOf("Default", "Serif", "Sans Serif")
        val fontAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, fontOptions)
        fontAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fontSpinner.adapter = fontAdapter
        fontSpinner.setSelection(fontOptions.indexOf(savedFont))

        textSizeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val textSize = progress + 10
                textSizeValue.text = "$textSize"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        applyButton.setOnClickListener {
            val editor = sharedPreferences.edit()
            val textSize = textSizeSeekBar.progress + 10
            val selectedFont = fontSpinner.selectedItem.toString()

            editor.putInt("textSize", textSize)
            editor.putString("font", selectedFont)
            editor.apply()

            // Reload MainActivity to apply changes
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
