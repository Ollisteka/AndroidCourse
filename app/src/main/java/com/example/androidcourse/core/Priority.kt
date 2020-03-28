package com.example.androidcourse.core

enum class Priority(val value: Int) {
    Low(0),
    Middle(1),
    High(2);

    companion object {
        private val values = values();
        fun getByValue(value: Int): Priority {
            return values.firstOrNull { it.value == value }
                ?: throw Exception("Class Priority doesn't contain value $value")
        }
    }
}