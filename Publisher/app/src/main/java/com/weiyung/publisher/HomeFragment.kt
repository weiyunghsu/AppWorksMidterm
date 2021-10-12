package com.weiyung.publisher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.weiyung.publisher.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel =
            ViewModelProvider(this).get(MainViewModel::class.java)
        _binding?.lifecycleOwner = this

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.home_recyclerview)

        val adapter = HomeAdapter(HomeAdapter.OnClickListener{})

        recyclerView.adapter = adapter

        val listener = SwipeRefreshLayout.OnRefreshListener {
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
            _binding!!.swipe.isRefreshing = false
        }
        _binding!!.swipe.setOnRefreshListener(listener)

        return binding.root


    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}