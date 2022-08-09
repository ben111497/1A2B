package com.example.wordle_1A2B.ui.component.fragment.homepage

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.wordle_1A2B.R
import com.example.wordle_1A2B.databinding.FragmentHomepageBinding
import com.example.wordle_1A2B.ui.base.BaseFragment
import com.example.wordle_1A2B.ui.factory.BaseModelFactory
import com.example.wordle_1A2B.utils.observe
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

@DelicateCoroutinesApi
class HomePageFragment: BaseFragment<HomepageViewModel, FragmentHomepageBinding>() {
    private lateinit var timer: Timer

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(requireActivity(), BaseModelFactory(requireActivity()))[HomepageViewModel::class.java]
    }

    override fun initViewBinding() {
        binding = FragmentHomepageBinding.inflate(layoutInflater)
    }

    override fun argument(bundle: Bundle?) {}

    override fun observeViewModel() {
        observe(viewModel.timerCount) {
            if (it < viewModel.getViewList().size) {
                val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.slid)
                viewModel.getViewList()[it].clearAnimation()
                viewModel.getViewList()[it].startAnimation(anim)
            }
        }

        observe(viewModel.coin) {
            binding?.tvCoin?.text = "$it"
        }
    }

    override fun init() {
        startTitleAnimate()
        viewModel.getLocalCoin()
    }

    override fun setListener() {
        binding?.run {
            cl4Words.setOnClickListener { switchToSelectedFragment(it, 4) }
            cl5Words.setOnClickListener { switchToSelectedFragment(it, 5) }
            cl6Words.setOnClickListener { switchToSelectedFragment(it, 6) }
            //tvCoin.setOnClickListener { viewModel.setCoin("", viewModel.getCoin() + 10) }
        }
    }

    private fun startTitleAnimate() {
        binding?.run { viewModel.addViews(arrayListOf<View>(imgTitle1, imgTitleA, imgTitle2, imgTitleB)) }
        timer = Timer().also {
            it.schedule(object : TimerTask() {
                override fun run() {
                    GlobalScope.launch(Dispatchers.Main) { viewModel.addCount() }
                } }, 250, 500) }
    }

    private fun stopTitleAnimate() {
        viewModel.setCount(0)
        timer.purge()
        timer.cancel()
    }

    private fun switchToSelectedFragment(view: View, word: Int) {
        stopTitleAnimate()
        Navigation.findNavController(view).navigate(R.id.action_homePageFragment_to_gameModeSelectFragment, Bundle().also { it.putInt("Word", word) })
    }
}