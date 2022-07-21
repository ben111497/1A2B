package com.example.wordle_1A2B.ui.component.dialog.show_message

import android.view.LayoutInflater
import androidx.lifecycle.ViewModel
import com.example.wordle_1A2B.databinding.DialogMessageBinding
import com.example.wordle_1A2B.ui.base.BaseDialogFragment

class ShowMessageDialog<viewModel: ViewModel>(private val created: (ShowMessageDialog<viewModel>) -> Unit)
    : BaseDialogFragment<viewModel, DialogMessageBinding>(0.75) {
    private lateinit var listener: Listener

    interface Listener {
        fun onOk()
    }

    override fun initViewBinding() {
        binding = DialogMessageBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun initViewModel() {}

    override fun observeViewModel() {}

    override fun setListener() {
        binding?.run {
            tvOk.setOnClickListener { listener.onOk() }
            tvCancel.setOnClickListener { dismiss() }
        }
    }

    override fun init() { created(this) }

    fun setListener(l: Listener) { listener = l }

    fun setTitle(str: String) { binding?.tvMessage?.text = str }
}
