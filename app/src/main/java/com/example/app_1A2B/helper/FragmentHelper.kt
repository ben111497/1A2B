package com.example.app_1A2B.helper

import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.app_1A2B.R
import com.example.app_1A2B.tools.ViewAnimation

fun FragmentActivity.setOnBackPressed(fragment: Fragment, result: () -> Unit) {
    this.onBackPressedDispatcher.addCallback(fragment, object: OnBackPressedCallback(true) {
        override fun handleOnBackPressed() { result() }
    })
}

fun Fragment.showCoinToast(root: ConstraintLayout, isGetCoin: Boolean, coin: Int, duration: Long) {
    val view = layoutInflater.inflate(R.layout.toast_coin, null)
    view.findViewById<TextView>(R.id.tvStatus).text = if (isGetCoin) "獲得" else "失去"
    view.findViewById<TextView>(R.id.tvCoin).text = "$coin"
    root.addView(view)

    val clParams = ConstraintLayout.LayoutParams(ConstraintSet.WRAP_CONTENT, ConstraintSet.WRAP_CONTENT)
    clParams.startToStart = root.id
    clParams.endToEnd = root.id
    clParams.bottomToBottom = root.id
    clParams.marginStart = this.requireActivity().getPixel(16)
    clParams.marginEnd = this.requireActivity().getPixel(16)
    clParams.bottomMargin = this.requireActivity().getPixel(16)

    view.layoutParams = clParams

    ViewAnimation.toastSlideUp(this.requireActivity(), view, duration) {
        if (it == ViewAnimation.AnimationStatus.OnAnimationEnd) root.removeView(view)
    }
}
