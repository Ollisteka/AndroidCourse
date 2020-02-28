package com.example.androidcourse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

const val COUNTER = "COUNTER"
const val EXTRA_COUNTER = "com.example.androidcourse.COUNTER"

class MainActivity : AppCompatActivity() {
    private var counter: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        printCounter()
        findViewById<Button>(R.id.squareButton).setOnClickListener {
            val sendIntent = Intent(applicationContext, SquareActivity::class.java)
            sendIntent.putExtra(EXTRA_COUNTER, counter)
            startActivity(sendIntent)
        }
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
        findViewById<TextView>(R.id.counter).text = "$counter"
    }
}
