package com.example.wordle_1A2B.ui.component.fragment.game.game

import android.os.Bundle
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.wordle_1A2B.R
import com.example.wordle_1A2B.databinding.FragmentGameBinding
import com.example.wordle_1A2B.ui.base.BaseFragment
import com.example.wordle_1A2B.ui.component.adapter.GameAdapter
import com.example.wordle_1A2B.ui.component.dialog.game.GameDialog
import com.example.wordle_1A2B.ui.component.dialog.game.GameHintDialog
import com.example.wordle_1A2B.ui.component.dialog.show_message.ShowMessageDialog
import com.example.wordle_1A2B.ui.factory.BaseModelFactory
import com.example.wordle_1A2B.helper.observe
import com.example.wordle_1A2B.helper.setOnBackPressed
import com.example.wordle_1A2B.helper.showToast
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class GameFragment: BaseFragment<GameViewModel, FragmentGameBinding>() {
    private var adapter: GameAdapter? = null

    override fun initViewModel() {
        requireActivity().let { viewModel = ViewModelProviders.of(it, BaseModelFactory(it))[GameViewModel::class.java] }
    }

    override fun initViewBinding() {
        binding = FragmentGameBinding.inflate(layoutInflater)
    }

    override fun argument(bundle: Bundle?) {}

    override fun observeViewModel() {
        observe(viewModel.viewList) {
            if (adapter == null) {
                adapter = GameAdapter(requireContext(), viewModel.word, viewModel.gameMode, viewModel.getViewList())
                binding?.lvGame?.adapter = adapter
            } else {
                adapter?.notifyDataSetChanged()
                binding?.lvGame?.smoothScrollToPosition(viewModel.getReplyCount() + 1)
            }
        }

        observe(viewModel.isDialogOn) {
            if (it) {
                GameDialog(viewModel.winCoin).also { it.setListener(object: GameDialog.Listener{
                    override fun onOk() {}

                    override fun onLeave() {
                        Navigation.findNavController(binding?.root ?: return).popBackStack(R.id.homePageFragment, false)
                    }
                }) }.show(requireActivity().supportFragmentManager, "hi")
            }
        }

        observe(viewModel.message) { if (it.isNotEmpty()) requireContext().showToast(it) }
        observe(viewModel.replyCount) { binding?.tvStep?.text = "Step ${it + 1}" }
    }

    override fun init() {
        viewModel.initViewModelData()

        if (adapter == null) {
            adapter = GameAdapter(requireContext(), viewModel.word, viewModel.gameMode, viewModel.getViewList())
            binding?.lvGame?.adapter = adapter
        } else {
            adapter?.notifyDataSetChanged()
            binding?.lvGame?.smoothScrollToPosition(viewModel.getReplyCount())
        }
    }

    override fun setListener() {
        requireActivity().setOnBackPressed(this) { leave() }

        binding?.run {
            tvClear.setOnClickListener { viewModel.clearAnswer() }
            tvConfirm.setOnClickListener { viewModel.confirmAnswer() }
            tvBack.setOnClickListener { viewModel.backAnswer() }
            imgLeave.setOnClickListener { leave() }
            imgHint.setOnClickListener { GameHintDialog().show(requireActivity().supportFragmentManager, "yo") }

            for (i in 0 .. 9) { clControl.findViewWithTag<TextView>("$i").setOnClickListener(viewModel.getClickObject()) }
        }
    }

    private fun leave() {
        if (viewModel.isGameWin) {
            Navigation.findNavController(binding?.root ?: return).popBackStack(R.id.homePageFragment, false)
        } else {
            ShowMessageDialog<ViewModel> {
                it.setTitle("遊戲尚未結束，是否要離開？\n離開將會扣除50金幣")
                it.setListener(object: ShowMessageDialog.Listener {
                    override fun onOk() {
                        viewModel.setReduceCoin(50)
                        it.dismiss()
                        Navigation.findNavController(binding?.root ?: return).popBackStack(R.id.homePageFragment, false)
                    }
                })
            }.show(requireActivity().supportFragmentManager, "message")
        }
    }
}