package com.example.wordle_1A2B.ui.component.fragment.game

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.wordle_1A2B.data.local.LocalData
import com.example.wordle_1A2B.databinding.FragmentGameBinding
import com.example.wordle_1A2B.ui.base.BaseFragment
import com.example.wordle_1A2B.ui.component.adapter.GameAdapter
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

    override fun argument(bundle: Bundle?) {
        bundle?.let {
            viewModel.setWordNumber(it.getInt("Word"))
        }
    }

    override fun observeViewModel() {
        observe(viewModel.viewList) {
            if (adapter == null) {
                adapter = GameAdapter(requireContext(), viewModel.word, viewModel.getViewList())
                binding?.lvGame?.adapter = adapter
            } else {
                adapter?.notifyDataSetChanged()
            }
        }

        observe(viewModel.message) { if (it.isNotEmpty()) requireContext().showToast(it) }
    }

    override fun init() {
        viewModel.setReplyCount(0)
        viewModel.setAnswer()
        viewModel.initViewList()

        if (adapter == null) {
            adapter = GameAdapter(requireContext(), viewModel.word, viewModel.getViewList())
            binding?.lvGame?.adapter = adapter
        } else {
            adapter?.notifyDataSetChanged()
            binding?.lvGame?.smoothScrollToPosition(viewModel.getReplyCount())
        }

        requireActivity().onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isGameEnd) {
                    Navigation.findNavController(binding?.root ?: return).popBackStack()
                } else {
                    requireContext().showToast("遊戲尚未結束，是否要離開")
                }
            }
        })
    }

    override fun setListener() {
        binding?.run {
            tvClear.setOnClickListener { viewModel.clearAnswer() }
            tvConfirm.setOnClickListener { viewModel.confirmAnswer() }

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
    }
}