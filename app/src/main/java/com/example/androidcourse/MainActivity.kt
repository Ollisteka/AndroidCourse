package com.example.androidcourse

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

const val COUNTER = "COUNTER"

class MainActivity : AppCompatActivity() {
    private var counter: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        printCounter()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(COUNTER, counter + 1)

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        counter = savedInstanceState.getInt(COUNTER, 0)
        printCounter()
    }

    private fun printCounter() {
        val textView = findViewById<TextView>(R.id.counter)
        textView.text = "$counter"
    }
}
