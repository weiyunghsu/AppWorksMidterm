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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.auth.User
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
    private var fireDB = FirebaseDatabase.getInstance()
    private var dbRef: DatabaseReference = fireDB.getReference("data")
    private var list: MutableList<Author> = mutableListOf()

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

        //獲得數據
        db.collection("data")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    Log.d(ContentValues.TAG, "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents: ", exception)
            }

        val listener = SwipeRefreshLayout.OnRefreshListener {
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
            _binding!!.swipe.isRefreshing = false

            //監聽即時data
            db.collection("data")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w(ContentValues.TAG, "listen:error", e)
                        return@addSnapshotListener
                    }

                    for (dc in snapshots!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
//                                val author = dc.getValue(Author::class.java)
//                                list.add(author!!)
                                Log.d(ContentValues.TAG, "New data: ${dc.document.data}")
                            }

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