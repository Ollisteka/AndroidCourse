package com.example.androidcourse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

const val COUNTER = "COUNTER"
const val EXTRA_COUNTER = "com.example.androidcourse.COUNTER"
const val LOG_TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private var counter: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(LOG_TAG, "Activity created")

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

    override fun onStart() {
        super.onStart()
        Log.d(LOG_TAG, "Activity started")
    }

    override fun onPause() {
        super.onPause()
        Log.d(LOG_TAG, "Activity paused")
    }

    override fun onResume() {
        super.onResume()
        Log.d(LOG_TAG, "Activity resumed")
    }

    override fun onStop() {
        super.onStop()
        Log.d(LOG_TAG, "Activity stopped")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(LOG_TAG, "Activity restarted")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "Activity destroyed")
    }
}
