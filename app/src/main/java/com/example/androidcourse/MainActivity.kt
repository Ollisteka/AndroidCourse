package com.example.androidcourse

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var counter: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        counter++
        val textView = findViewById<TextView>(R.id.counter)
        textView.text = "$counter"
    }

}
