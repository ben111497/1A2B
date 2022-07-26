package com.example.wordle_1A2B.ui.component.fragment.game.game

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.wordle_1A2B.R
import com.example.wordle_1A2B.data.local.LocalData
import com.example.wordle_1A2B.databinding.FragmentGameBinding
import com.example.wordle_1A2B.ui.base.BaseFragment
import com.example.wordle_1A2B.ui.component.adapter.GameAdapter
import com.example.wordle_1A2B.ui.component.dialog.game.GameDialog
import com.example.wordle_1A2B.ui.component.dialog.show_message.ShowMessageDialog
import com.example.wordle_1A2B.ui.component.fragment.game.GameRepository
import com.example.wordle_1A2B.ui.component.fragment.game.GameViewModel
import com.example.wordle_1A2B.ui.factory.BaseModelFactory
import com.example.wordle_1A2B.utils.observe
import com.example.wordle_1A2B.utils.showToast

class GameFragment: BaseFragment<GameViewModel, FragmentGameBinding>() {
    private var adapter: GameAdapter? = null

    override fun initViewModel() {
        requireActivity().let { viewModel = ViewModelProviders.of(it, BaseModelFactory(GameRepository(LocalData(it))))[GameViewModel::class.java] }
    }

    override fun initViewBinding() {
        binding = FragmentGameBinding.inflate(layoutInflater)
    }

    override fun argument(bundle: Bundle?) {}

    override fun observeViewModel() {
        observe(viewModel.viewList) {
            if (adapter == null) {
                adapter = GameAdapter(requireContext(), viewModel.word, viewModel.getViewList())
                binding?.lvGame?.adapter = adapter
            } else {
                adapter?.notifyDataSetChanged()
                binding?.lvGame?.smoothScrollToPosition(viewModel.getReplyCount() + 1)
            }
        }

        observe(viewModel.isDialogOn) {
            if (it) {
                GameDialog().also { it.setListener(object: GameDialog.Listener{
                    override fun onOk() {}

                    override fun onLeave() {
                        Navigation.findNavController(binding?.root ?: return).popBackStack()
                    }
                }) }.show(requireActivity().supportFragmentManager, "hi")
            }
        }

        observe(viewModel.message) { if (it.isNotEmpty()) requireContext().showToast(it) }
        observe(viewModel.replyCount) { binding?.tvStep?.text = "Step ${it + 1}" }
    }

    override fun init() {
        viewModel.initViewList()
        viewModel.setReplyCount(0)
        viewModel.setAnswer()
        viewModel.setIsDialogOn(false)
        viewModel.setMessage("")

        if (adapter == null) {
            adapter = GameAdapter(requireContext(), viewModel.word, viewModel.getViewList())
            binding?.lvGame?.adapter = adapter
        } else {
            adapter?.notifyDataSetChanged()
            binding?.lvGame?.smoothScrollToPosition(viewModel.getReplyCount())
        }
    }

    override fun setListener() {
        binding?.run {
            tvClear.setOnClickListener { viewModel.clearAnswer() }
            tvConfirm.setOnClickListener { viewModel.confirmAnswer() }
            tvBack.setOnClickListener { viewModel.backAnswer() }
            imgLeave.setOnClickListener { leave() }

            tv1.setOnClickListener(viewModel.getClickObject())
            tv2.setOnClickListener(viewModel.getClickObject())
            tv3.setOnClickListener(viewModel.getClickObject())
            tv4.setOnClickListener(viewModel.getClickObject())
            tv5.setOnClickListener(viewModel.getClickObject())
            tv6.setOnClickListener(viewModel.getClickObject())
            tv7.setOnClickListener(viewModel.getClickObject())
            tv8.setOnClickListener(viewModel.getClickObject())
            tv9.setOnClickListener(viewModel.getClickObject())
            tv0.setOnClickListener(viewModel.getClickObject())
        }


        requireActivity().onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { leave() }
        })
    }

    private fun leave() {
        if (viewModel.isGameWin) {
            Navigation.findNavController(binding?.root ?: return).navigate(R.id.action_gameFragment_to_homePageFragment)
        } else {
            ShowMessageDialog<ViewModel> {
                it.setTitle("遊戲尚未結束，是否要離開？")
                it.setListener(object: ShowMessageDialog.Listener {
                    override fun onOk() {
                        it.dismiss()
                        Navigation.findNavController(binding?.root ?: return).navigate(R.id.action_gameFragment_to_homePageFragment)
                    }
                })
            }.show(requireActivity().supportFragmentManager, "message")
        }
    }
}