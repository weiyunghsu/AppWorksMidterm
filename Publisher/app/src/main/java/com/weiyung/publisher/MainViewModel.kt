package com.weiyung.publisher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class MainViewModel : ViewModel(){
    private var fireDB = FirebaseDatabase.getInstance()
    private var dbRef: DatabaseReference = fireDB.getReference("data")
    var listC: MutableList<Content> = mutableListOf()
    var listA: MutableList<Author> = mutableListOf()

    private val _contentLivaData = MutableLiveData<Content>()
    val contentLiveData: LiveData<Content>
        get() = _contentLivaData
    private val _authorLivaData = MutableLiveData<Author>()
    val authorLiveData: LiveData<Author>
        get() = _authorLivaData

    val titleA = _contentLivaData.value?.title.toString()
    val authorA = _authorLivaData.value?.name.toString()
    val create_timeA = _contentLivaData.value?.created_time.toString()
    val contentA = _contentLivaData.value?.content.toString()
    val categoryA = _contentLivaData.value?.category.toString()

    val db = Firebase.firestore

    fun addData() {
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

}
