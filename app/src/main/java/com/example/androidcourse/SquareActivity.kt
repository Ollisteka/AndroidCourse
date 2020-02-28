package com.example.androidcourse

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SquareActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_square)

        val counter = intent.getIntExtra(EXTRA_COUNTER, 0)
        val squared = counter * counter

        findViewById<TextView>(R.id.squaredCounterLabel).text = "Квадрат числа $counter:"
        findViewById<TextView>(R.id.squaredCounter).text = "$squared"
    }
}
