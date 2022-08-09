package com.example.wordle_1A2B.data.dto

enum class GameResultStatus {
    NotComparison, Error, PositionError, Correct
}

enum class GameMode {
    //不重複且沒有提示、重複但沒有提示、不重複但有提示、重複且有提示
    No, Repeat, Hint, RepeatAndHint;
    //150, 200, 100, 50 價格
    companion object {
        fun getByValue(value: Int) = values().firstOrNull { it.ordinal == value }
        fun getSize() = values().size
    }
}