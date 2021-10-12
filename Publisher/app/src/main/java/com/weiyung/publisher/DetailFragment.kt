package com.weiyung.publisher

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.weiyung.publisher.databinding.FragmentDetailBinding
import java.time.Instant
import java.time.format.DateTimeFormatter

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private lateinit var viewModel: MainViewModel
    private val binding get() = _binding!!
    val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel =
            ViewModelProvider(this).get(MainViewModel::class.java)
        _binding?.lifecycleOwner = this

        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        binding.submitButton.setOnClickListener {
            val editTitle = binding.inputTitle.text.toString()
            val editCategory = binding.inputCategory.text.toString()
            val editContent = binding.inputContent.text.toString()
            val createTime = DateTimeFormatter.ISO_INSTANT.format(Instant.now())
            val newID = db.collection("data").document().id
            val authorList = Author("","","")
            val authorA = Author("123@123.com","123456","Wei")
            val authorB = Author("","","")
            val contexxtt = Content(
                authorList, editTitle,editContent,
                FieldValue.serverTimestamp(),newID,editCategory)
            binding.viewModel?.addData()

            db.collection("data").document(newID)
                .set(contexxtt)
                .addOnSuccessListener {
//                    if (editTitle == "123"){
//                    }
                    Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
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