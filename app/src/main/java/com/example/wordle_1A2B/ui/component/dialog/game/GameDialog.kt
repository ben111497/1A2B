package com.example.wordle_1A2B.ui.component.dialog.game

import android.view.*
import androidx.lifecycle.ViewModel
import com.example.wordle_1A2B.databinding.DialogGameVictoryBinding
import com.example.wordle_1A2B.ui.base.BaseDialogFragment

class GameDialog(val coin: Int): BaseDialogFragment<ViewModel, DialogGameVictoryBinding>(0.75) {
    private lateinit var listener: Listener

    interface Listener {
        fun onOk()
        fun onLeave()
    }

    override fun initViewBinding() {
        binding = DialogGameVictoryBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun initViewModel() {}

    override fun observeViewModel() {}

    override fun setListener() {
        binding?.run {
            tvOk.setOnClickListener {
                listener.onOk()
                dismiss()
            }
            tvLeave.setOnClickListener {
                listener.onLeave()
                dismiss()
            }
        }
    }

    override fun init() {
        binding?.tvCoin?.text = "獲得 $coin 金幣"
    }

    fun setListener(l: Listener) { listener = l }
}
