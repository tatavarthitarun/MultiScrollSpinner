package com.tatav.multiscrollspinner

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

/**
 * Demo activity showcasing the MultiScrollSpinner widget.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupSpinner()
    }

    private fun setupSpinner() {
        val spinner = findViewById<MultiScrollSpinner>(R.id.multiScrollSpinner)
        
        val items = listOf(
            "Short",
            "Medium length item",
            "This is a very long item that will require horizontal scrolling to see the full text",
            "Another item",
            "Yet another medium length item here",
            "Item 6",
            "This is an extremely long item that definitely needs horizontal scrolling because it contains a lot of text that won't fit on a single line",
            "Short item",
            "Medium item text",
            "Long item with lots of text content that extends beyond normal width",
            "Item 11",
            "Item 12",
            "Item 13",
            "Item 14",
            "Item 15",
            "Item 16",
            "Item 17",
            "Item 18",
            "Item 19",
            "Item 20 - Last item"
        )
        
        spinner.setItems(items)
        spinner.setOnItemSelectedListener { position, item ->
            Toast.makeText(
                this,
                "Selected: $item (Position: $position)",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
