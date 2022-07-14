package com.example.wordle_1A2B.fragment.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.wordle_1A2B.R
import com.example.wordle_1A2B.databinding.FragmentHomepageBinding
import com.example.wordle_1A2B.fragment.factory.BaseModelFactory
import com.example.wordle_1A2B.fragment.repository.HomepageLocalRepository
import com.example.wordle_1A2B.fragment.repository.HomepageRepository
import com.example.wordle_1A2B.fragment.viewModel.HomepageViewModel
import com.example.wordle_1A2B.utils.observe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class HomePageFragment: Fragment() {
    private var binding: FragmentHomepageBinding? = null
    private val viewModel: HomepageViewModel by lazy {
        ViewModelProviders.of(requireActivity(),
            BaseModelFactory(HomepageRepository(requireActivity(), HomepageLocalRepository(requireContext())))).get(HomepageViewModel::class.java)
    }

    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomepageBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelObserve()
        setListener()
        init()
    }

    private fun init() {
        startTitleAnimate()
    }

    private fun setListener() {
        binding?.run {
            cl4Words.setOnClickListener { stopTitleAnimate() }
        }
    }

    private fun viewModelObserve() {
        observe(viewModel.timerCount) {
            if (it < viewModel.getViewList().size) {
                val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.slid)
                viewModel.getViewList()[it].clearAnimation()
                viewModel.getViewList()[it].startAnimation(anim)
            }
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