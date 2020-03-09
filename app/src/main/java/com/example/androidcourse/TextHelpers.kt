package com.example.androidcourse

import kotlin.math.abs

class TextHelpers {
    companion object {
        fun getPluralWord(n: Int, pluralsWords: Array<String>): String {
            return when (n) {
                0 -> pluralsWords[0]
                1 -> pluralsWords[1]
                2 -> pluralsWords[2]
                5 -> pluralsWords[3]
                else -> getPluralWord(plurals(n), pluralsWords)
            }
        }

        private fun plurals(n: Int): Int {
            var local = n
            if (local == 0) return 0
            local = abs(local) % 100
            val mod = local % 10
            return when {
                local in 11..19 -> 5
                mod in 2..4 -> 2
                mod == 1 -> 1
                else -> 5
            }
        }
    }
}