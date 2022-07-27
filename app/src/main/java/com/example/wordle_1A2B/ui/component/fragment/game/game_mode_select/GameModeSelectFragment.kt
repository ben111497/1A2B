package com.example.wordle_1A2B.ui.component.fragment.game.game_mode_select

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.example.wordle_1A2B.R
import com.example.wordle_1A2B.data.dto.GameMode
import com.example.wordle_1A2B.data.local.LocalData
import com.example.wordle_1A2B.databinding.FragmentGameModeSelectBinding
import com.example.wordle_1A2B.tools.DoubleAddPageTransformer
import com.example.wordle_1A2B.ui.base.BaseFragment
import com.example.wordle_1A2B.ui.component.adapter.GameModeAdapter
import com.example.wordle_1A2B.ui.component.fragment.game.GameRepository
import com.example.wordle_1A2B.ui.component.fragment.game.GameViewModel
import com.example.wordle_1A2B.ui.factory.BaseModelFactory

class GameModeSelectFragment: BaseFragment<GameViewModel, FragmentGameModeSelectBinding>() {
    private var adapter: GameModeAdapter? = null
    override fun initViewBinding() {
        binding = FragmentGameModeSelectBinding.inflate(layoutInflater)
    }

    override fun initViewModel() {
        requireActivity().let { viewModel = ViewModelProviders.of(it, BaseModelFactory(GameRepository(LocalData(it))))[GameViewModel::class.java] }
    }

    override fun argument(bundle: Bundle?) {
        bundle?.let { viewModel.setWordNumber(it.getInt("Word")) }
    }

    override fun observeViewModel() {}

    override fun init() {
        if (adapter == null) {
            adapter = GameModeAdapter(requireContext(), arrayListOf<GameMode>(GameMode.No, GameMode.Repeat, GameMode.Hint, GameMode.RepeatAndHint))
            adapter?.setListener(object: GameModeAdapter.Listener {
                override fun onEnterClick(data: GameMode) {
                    viewModel.gameMode = data
                    Navigation.findNavController(binding?.root ?: return).navigate(R.id.action_gameModeSelectFragment_to_gameFragment)
                }
            })

            binding?.vpMode?.offscreenPageLimit = 1
            binding?.vpMode?.setPageTransformer(DoubleAddPageTransformer())
            binding?.vpMode?.adapter = adapter

            (Integer.MAX_VALUE / 2).also { position ->
                binding?.vpMode?.setCurrentItem(if (position % GameMode.getSize() == 0) position else position - position % GameMode.getSize(), false)
            }

            binding?.vpMode?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageScrollStateChanged(state: Int) {}

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewModel.gameMode = GameMode.getByValue(position % GameMode.getSize()) ?: return
                }
            })
        }
    }

    override fun setListener() {
        binding?.run {
            imgBack.setOnClickListener { Navigation.findNavController(binding?.root ?: return@setOnClickListener).popBackStack() }
        }
    }
}