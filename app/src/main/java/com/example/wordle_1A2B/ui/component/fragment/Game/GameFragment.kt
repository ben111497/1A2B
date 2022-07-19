package com.example.wordle_1A2B.ui.component.fragment.Game

import androidx.lifecycle.ViewModelProviders
import com.example.wordle_1A2B.data.local.LocalData
import com.example.wordle_1A2B.databinding.FragmentGameBinding
import com.example.wordle_1A2B.ui.base.BaseFragment
import com.example.wordle_1A2B.ui.component.adapter.GameAdapter
import com.example.wordle_1A2B.ui.factory.BaseModelFactory
import com.example.wordle_1A2B.utils.observe

class GameFragment: BaseFragment<GameViewModel, FragmentGameBinding>() {
    private var adapter: GameAdapter? = null

    override fun initViewModel() {
        requireActivity().let {
            viewModel = ViewModelProviders.of(it, BaseModelFactory(it, GameRepository(LocalData(it))))[GameViewModel::class.java]
        }
    }

    override fun initViewBinding() {
        binding = FragmentGameBinding.inflate(layoutInflater)
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
    }

    override fun init() {
        viewModel.setWordNumber(4)
        viewModel.setAnswer()
        viewModel.initViewList()
        if (adapter == null) {
            adapter = GameAdapter(requireContext(), viewModel.word, viewModel.getViewList())
            binding?.lvGame?.adapter = adapter
        } else {
            adapter?.notifyDataSetChanged()
        }
    }

    override fun setListener() {
        binding?.run {
            tvClear.setOnClickListener { viewModel.clearCurrentAnswer() }
            tvConfirm.setOnClickListener {  }

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