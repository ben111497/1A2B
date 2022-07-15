package com.example.wordle_1A2B.ui.component.fragment.homepage

import android.view.View
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProviders
import com.example.wordle_1A2B.R
import com.example.wordle_1A2B.data.local.LocalData
import com.example.wordle_1A2B.databinding.FragmentHomepageBinding
import com.example.wordle_1A2B.ui.base.BaseFragment
import com.example.wordle_1A2B.ui.factory.BaseModelFactory
import com.example.wordle_1A2B.utils.observe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class HomePageFragment: BaseFragment<HomepageViewModel, FragmentHomepageBinding>() {
    private lateinit var timer: Timer

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(requireActivity(), BaseModelFactory(HomepageRepository(LocalData(requireContext())))).get(HomepageViewModel::class.java)
    }

    override fun initViewBinding() {
        binding = FragmentHomepageBinding.inflate(layoutInflater)
    }

    override fun observeViewModel() {
        observe(viewModel.timerCount) {
            if (it < viewModel.getViewList().size) {
                val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.slid)
                viewModel.getViewList()[it].clearAnimation()
                viewModel.getViewList()[it].startAnimation(anim)
            }
        }
    }

    override fun init() {
        startTitleAnimate()
    }

    override fun setListener() {
        binding?.run {
            cl4Words.setOnClickListener { stopTitleAnimate() }
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
        timer.purge()
        timer.cancel()
    }
}