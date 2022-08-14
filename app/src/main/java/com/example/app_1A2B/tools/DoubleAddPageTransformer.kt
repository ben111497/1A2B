package com.example.app_1A2B.tools

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class DoubleAddPageTransformer: ViewPager2.PageTransformer {
    private var mOffset = 1.0f
    private var scale = 0.9f
    private var offScreenPageLimit = 1
    private var pagerWidth = 0
    private var horizontalOffsetBase = 0f

    override fun transformPage(page: View, position: Float) {
        if (position > 2 || position < -2) return
        animWay1(page, position)
        page.translationZ = -abs(position)
    }

    private fun animWay1(page: View, position: Float) {
        if (pagerWidth == 0) pagerWidth = page.width
        if (horizontalOffsetBase == 0f) horizontalOffsetBase = (pagerWidth - pagerWidth * scale) / offScreenPageLimit + mOffset

        page.translationX = (horizontalOffsetBase - pagerWidth) * position

        page.alpha = if (position <= -1 || position >= 1) 0.75f - abs((0.75f * (position / 2))) else 1.0f - abs(position) * 0.25f

        val scaleFactor = (scale - abs(position) * 0.1f).coerceAtMost(scale)
        page.scaleX = scaleFactor
        page.scaleY = scaleFactor
    }
}