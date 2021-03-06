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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.weiyung.publisher.databinding.FragmentDetailBinding
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

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
        viewModel.addData()

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

            addData()
            db.collection("articles").document(newID)
                .set(contexxtt)
                .addOnSuccessListener {
//                    if (editTitle == "123"){
//                    }
                    Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }
        return binding.root
    }

    fun addData() {
        val articles = FirebaseFirestore.getInstance()
            .collection("articles")
        val document = articles.document()
        val data = hashMapOf(
            "author" to hashMapOf(
                "email" to "wayne@school.appworks.tw",
                "id" to "waynechen323",
                "name" to "AKA????????????"
            ),
            "title" to "IU????????????????????????????????????????????????????????????????????????????????????????????????",
            "content" to "????????????IU???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????IG???????????????????????????????????????????????????????????????????????????????????????????????????????????????",
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