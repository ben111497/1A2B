package com.example.wordle_1A2B.helper

import android.content.Context
import android.view.View
import com.example.wordle_1A2B.tools.ViewAnimation

fun View.flop(context: Context, result: () -> Boolean) {
    ViewAnimation.rotationBack(context, this) { status ->
        if (status == ViewAnimation.AnimationStatus.OnAnimationEnd) {
            if (result()) ViewAnimation.rotationFont(context, this)
        }
    }
}