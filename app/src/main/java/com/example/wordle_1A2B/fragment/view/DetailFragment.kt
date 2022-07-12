package com.example.wordle_1A2B.fragment.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.wordle_1A2B.adapter.PictureBigAdapter
import com.example.wordle_1A2B.adapter.PictureSmallAdapter
import com.example.wordle_1A2B.databinding.FragmentDetailBinding
import com.example.wordle_1A2B.fragment.factory.BlankViewModelFactory
import com.example.wordle_1A2B.fragment.repository.BlankRepository
import com.example.wordle_1A2B.fragment.viewModel.BlankViewModel
import com.example.wordle_1A2B.tools.AsyncImageLoader


class DetailFragment : Fragment() {
    private var binding: FragmentDetailBinding? = null
    private val viewModel: BlankViewModel by lazy {
        ViewModelProviders.of(requireActivity(), BlankViewModelFactory(BlankRepository(requireActivity()))).get(BlankViewModel::class.java)
    }
    private var smallPictureAdapter: PictureSmallAdapter? = null
    private var bigPictureAdapter: PictureBigAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}

        //設定實體返回鍵
        requireActivity().onBackPressedDispatcher.addCallback(this, object: OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                binding?.run {
                    if (clLandscape.isVisible) clLandscape.visibility = View.GONE else Navigation.findNavController(root).popBackStack()
                }
            }
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListener()
    }

    private fun init() {
        binding?.run {
            tvTopic.text = viewModel.getListCategory()

            viewModel.getAttractionsList()?.get(viewModel.selectIndex)?.let { info ->
                tvTitle.text = info.name ?: ""
                info.images?.let { if (it.isNotEmpty()) AsyncImageLoader.loadImage(imgPicture, it[0].src ?: "") }
                info.name?.let { tvName.text = it }
                info.months?.let { tvMonth.text = if (it.isEmpty()) "無" else it }
                info.tel?.let { tvPhone.text = if (it.isEmpty()) "暫無提供" else it }
                info.address?.let { tvAddress.text = if (it.isEmpty()) "暫無提供" else it }
                info.open_time?.let { tvOpen.text = if (it.isEmpty()) "暫無提供" else it }
                info.introduction?.let { tvIntro.text = if (it.isEmpty()) "無" else it }
                info.ticket?.let { if (it.isEmpty()) gpTicket.visibility = View.GONE else tvTicket.text = it }
                info.remind?.let { if (it.isEmpty()) gpRemind.visibility = View.GONE else tvRemind.text = it }
                info.official_site?.let { if (it.isEmpty()) tvOffice.visibility = View.GONE }
                info.facebook?.let { if (it.isEmpty()) tvFacebook.visibility = View.GONE }
                info.url?.let { if (it.isEmpty()) tvMore.visibility = View.GONE }
            }
        }

        setRvSmallPicture()
        setBigPictureViewpager()
    }

    private fun setListener() {
        binding?.run {
            tvPhone.setOnClickListener {
                val uri = Uri.parse("tel:${tvPhone.text}")
                startActivity(Intent(Intent.ACTION_DIAL, uri))
            }

            tvAddress.setOnClickListener {
                viewModel.getAttractionsList()?.get(viewModel.selectIndex)?.let {
                    val uri = Uri.parse("geo:${it.nlat},${it.elong}")
                    startActivity(Intent(Intent.ACTION_VIEW, uri))
                }
            }

            tvOffice.setOnClickListener { loadUrl(viewModel.getAttractionsList()?.get(viewModel.selectIndex)?.official_site ?: return@setOnClickListener) }
            tvMore.setOnClickListener { loadUrl(viewModel.getAttractionsList()?.get(viewModel.selectIndex)?.url ?: return@setOnClickListener) }
            tvFacebook.setOnClickListener { loadUrl(viewModel.getAttractionsList()?.get(viewModel.selectIndex)?.facebook ?: return@setOnClickListener) }
            imgBack.setOnClickListener { Navigation.findNavController(it).popBackStack() }
            imgBack2.setOnClickListener { clLandscape.visibility = View.GONE }
        }
    }

    private fun setRvSmallPicture() {
        binding?.run {
            viewModel.getAttractionsList()?.get(viewModel.selectIndex)?.images?.let {
                if (it.size == 0) {
                    rvPicture.visibility = View.GONE
                    return
                } else rvPicture.visibility = View.VISIBLE

                smallPictureAdapter = PictureSmallAdapter(requireContext(), it)
                smallPictureAdapter?.setListener(object: PictureSmallAdapter.Listener{
                    override fun onClick(number: Int) {
                        vpPicture.setCurrentItem(number, false)
                        clLandscape.visibility = View.VISIBLE
                    }
                })

                rvPicture.layoutManager = LinearLayoutManager(requireContext()).also { it.orientation = LinearLayoutManager.HORIZONTAL }
                rvPicture.adapter = smallPictureAdapter
            } ?: kotlin.run { rvPicture.visibility = View.GONE }
        }
    }

    private fun setBigPictureViewpager() {
        binding?.run {
            viewModel.getAttractionsList()?.get(viewModel.selectIndex)?.images?.let {
                bigPictureAdapter = PictureBigAdapter(requireContext(), it)
                vpPicture.adapter = bigPictureAdapter

                vpPicture.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageScrollStateChanged(state: Int) {}

                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        tvPosition.text = "${position + 1}/${it.size}"
                    }
                })
            }
        }
    }

    private fun loadUrl(url: String) = startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}