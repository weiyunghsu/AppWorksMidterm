package com.weiyung.publisher

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.weiyung.publisher.databinding.FragmentHomeBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val db = Firebase.firestore

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

            db.collection("data")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w(ContentValues.TAG, "listen:error", e)
                        return@addSnapshotListener
                    }

                    for (dc in snapshots!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> Log.d(ContentValues.TAG, "New data: ${dc.document.data}")
                            DocumentChange.Type.MODIFIED -> Log.d(ContentValues.TAG, "Modified data: ${dc.document.data}")
                            DocumentChange.Type.REMOVED -> Log.d(ContentValues.TAG, "Removed data: ${dc.document.data}")
                        }
                    }
                }

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