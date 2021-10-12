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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.weiyung.publisher.databinding.FragmentHomeBinding
import java.util.*

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
        viewModel.addData()
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
            db.collection("articles")
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.w(ContentValues.TAG, "listen:error", e)
                        return@addSnapshotListener
                    }

                    for (dc in snapshots!!.documentChanges) {
                        when (dc.type) {
                            DocumentChange.Type.ADDED -> {
//                                updateData()
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

        viewModel.contentLiveData.observe(viewLifecycleOwner,{
            it?.let {
                adapter.submitList(viewModel.listC)
            }
        })
//        viewModel.authorLiveData.observe(viewLifecycleOwner,{
//            it?.let {
//                adapter.submitList(viewModel.listA)
//            }
//        })

        return binding.root
    }


    fun updateData() {
        val articles = FirebaseFirestore.getInstance()
            .collection("articles")
        val document = articles.document()
        val data = hashMapOf(
            "author" to hashMapOf(
                "email" to "wayne@school.appworks.tw",
                "id" to "waynechen323",
                "name" to "AKA小安老師"
            ),
            "title" to "IU「亂穿」竟美出新境界！笑稱自己品味奇怪　網笑：靠顏值撐住女神氣場",
            "content" to "南韓歌手IU（李知恩）無論在歌唱方面或是近期的戲劇作品都有亮眼的成績，但俗話說人無完美、美玉微瑕，曾再跟工作人員的互動影片中坦言自己品味很奇怪，近日在IG上分享了宛如「媽媽們青春時代的玉女歌手」超復古穿搭造型，卻意外美出新境界。",
            "createdTime" to Calendar.getInstance()
                .timeInMillis,
            "id" to document.id,
            "category" to "Beauty"
        )
        document.set(data)
    }
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}