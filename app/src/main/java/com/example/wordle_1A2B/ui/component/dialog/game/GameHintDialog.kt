package com.example.wordle_1A2B.ui.component.dialog.game

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.wordle_1A2B.R
import com.example.wordle_1A2B.databinding.DialogGameHintBinding
import com.example.wordle_1A2B.ui.base.BaseDialogFragment
import com.example.wordle_1A2B.ui.component.dialog.show_message.ShowMessageDialog
import com.example.wordle_1A2B.ui.component.fragment.game.game.GameViewModel
import com.example.wordle_1A2B.ui.factory.BaseModelFactory
import com.example.wordle_1A2B.helper.flop
import com.example.wordle_1A2B.helper.observe
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class GameHintDialog: BaseDialogFragment<GameViewModel, DialogGameHintBinding>(0.75) {
    override fun initViewBinding() {
        binding = DialogGameHintBinding.inflate(LayoutInflater.from(requireContext()))
    }

    override fun initViewModel() {
        requireActivity().let { viewModel = ViewModelProviders.of(it, BaseModelFactory(it))[GameViewModel::class.java] }
    }

    override fun observeViewModel() {
        viewLifecycleOwner.observe(viewModel.hintList) {
            if (it.size == 0) return@observe
            val view = binding?.root?.findViewWithTag<TextView>("${it[it.lastIndex].position + 1}")
            view?.flop(requireActivity()) {
                view.text = "${it[it.lastIndex].answer}"
                view.isActivated = true
                view.setBackgroundResource(R.drawable.bg_game_correct)
                view.setTextColor(requireActivity().getColor(R.color.white_FFFFFF))
                true
            }
        }
    }

    override fun setListener() {
        binding?.run {
            for (i in 1 .. viewModel.word) { root.findViewWithTag<TextView>("$i").setOnClickListener(getHintClickObject()) }
        }
    }

    override fun init() {
        binding?.run {
            tvAnswer5.visibility = if (viewModel.word >= 5) View.VISIBLE else View.GONE
            tvAnswer6.visibility = if (viewModel.word >= 6) View.VISIBLE else View.GONE

            viewModel.getHintList().forEachIndexed { _, hint ->
                binding?.root?.findViewWithTag<TextView>("${hint.position + 1}")?.let {
                    it.text = "${hint.answer}"
                    it.setBackgroundResource(R.drawable.bg_game_correct)
                    it.setTextColor(requireActivity().getColor(R.color.white_FFFFFF))
                    it.isActivated = true
                }
            }
        }
    }

    private fun getHintClickObject(): View.OnClickListener {
        return View.OnClickListener { v ->
            v?.let { view ->
                if (!view.isActivated) showHintCheckDialog(view.tag.toString().toInt())
            }
        }
    }

    private fun showHintCheckDialog(position: Int) {
        ShowMessageDialog<ViewModel> {
            it.setTitle("使用提示將扣除50金幣\n確定要使用？")
            it.setListener(object: ShowMessageDialog.Listener {
                override fun onOk() {
                    viewModel.useHint(position - 1)
                    viewModel.setReduceCoin(50)
                    it.dismiss()
                }
            })
        }.show(requireActivity().supportFragmentManager, "hint")
    }
}