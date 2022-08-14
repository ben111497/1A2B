package com.example.app_1A2B.data.dto

data class GameClass(var words: Int, val reply: ArrayList<Reply>) {
    data class Reply(var answer: ArrayList<Int>, var result: ArrayList<GameResultStatus>)
}