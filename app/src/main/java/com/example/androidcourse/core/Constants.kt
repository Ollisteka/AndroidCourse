package com.example.androidcourse.core

import java.util.*

class EXTRA {
    companion object {
        const val HABIT_ID = "com.example.androidcourse.HABIT_ID"
        const val HABIT_TYPE = "com.example.androidcourse.HABIT_TYPE"
    }
}

class LOG_TAGS {
    companion object {
        const val DATABASE = "androidcourse.DATABASE"
        const val NETWORK = "androidcourse.NETWORK"
    }
}

val EMPTY_UUID: UUID = UUID.fromString("4ebab42e-5be2-416b-971b-7e292db5946a")