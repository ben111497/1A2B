package com.example.wordle_1A2B.ui.component.dialog.game

import android.view.*
import androidx.lifecycle.ViewModelProviders
import com.example.wordle_1A2B.databinding.DialogGameVictoryBinding
import com.example.wordle_1A2B.ui.base.BaseDialogFragment

class GameDialogFragment: BaseDialogFragment<GameDialogViewModel, DialogGameVictoryBinding>(0.75) {
    override fun initViewBinding() {
        binding = DialogGameVictoryBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(requireActivity())[GameDialogViewModel::class.java]
    }

    override fun observeViewModel() {}

    override fun setListener() {
        binding?.run {
            clOk.setOnClickListener { dismiss() }
        }
    }

    override fun init() {}
}
