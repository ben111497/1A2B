package com.example.wordle_1A2B.fragment.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.wordle_1A2B.R
import com.example.wordle_1A2B.adapter.ListAdapter
import com.example.wordle_1A2B.databinding.FragmentHomeBinding
import com.example.wordle_1A2B.fragment.factory.BlankViewModelFactory
import com.example.wordle_1A2B.fragment.repository.BlankRepository
import com.example.wordle_1A2B.fragment.viewModel.BlankViewModel
import com.example.wordle_1A2B.utils.getPixel
import com.example.wordle_1A2B.utils.getScreenWidthPixel
import com.example.wordle_1A2B.utils.observe

class HomeFragment : Fragment() {
    private var binding: FragmentHomeBinding? = null
    private var adapter: ListAdapter? = null
    private val viewModel: BlankViewModel by lazy {
        ViewModelProviders.of(requireActivity(), BlankViewModelFactory(BlankRepository(requireActivity()))).get(BlankViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelObserve()
        setListener()
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    private fun init() {
        if (viewModel.getAttractionsList()?.size == 0) viewModel.changePage(1)
    }

    private fun setListener() {
        binding?.run {
            btnNext.setOnClickListener { viewModel.changePage(viewModel.getPage() + 1) }
            btnLast.setOnClickListener { viewModel.changePage(viewModel.getPage() - 1) }
        }
    }

    private fun viewModelObserve() {
        observe(viewModel.attractionsList) {
            if (adapter == null) {
                adapter = ListAdapter(requireActivity(), viewModel.getAttractionsList() ?: ArrayList())
                adapter?.setListener(object: ListAdapter.Listener{
                    override fun onItemClick(index: Int) {
                        viewModel.selectIndex = index
                        adapter = null
                        binding?.listView?.let { it1 -> Navigation.findNavController(it1).navigate(R.id.action_homeFragment_to_detailFragment2) }
                    }
                })

                binding?.listView?.adapter = adapter
                setListViewHeight()
            } else {
                binding?.listView?.post {
                    setListViewHeight()
                    binding?.svPage?.scrollTo(0, 0)
                }
                adapter?.notifyDataSetChanged()
            }
        }

        observe(viewModel.page) {
            binding?.let { bind ->
                bind.btnLast.visibility = if (it <= 1) View.GONE else View.VISIBLE
                bind.btnNext.visibility = if (it * 30 >= viewModel.total) View.GONE else View.VISIBLE
            }
        }
    }

    private fun setListViewHeight() {
        binding?.listView?.let { list ->
            val item = adapter?.getView(0, null, list) ?: return
            val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            val widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(requireContext().getScreenWidthPixel(), View.MeasureSpec.EXACTLY)
            item.measure(widthMeasureSpec, heightMeasureSpec)
            list.layoutParams.height = (viewModel.getAttractionsList()?.size ?: 0) * (item.measuredHeight + requireActivity().getPixel(8))
        }
    }
}