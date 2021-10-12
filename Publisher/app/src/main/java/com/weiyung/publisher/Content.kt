package com.weiyung.publisher

import com.google.firebase.firestore.FieldValue

data class Content(
    val author: Author,
    val title: String,
    val content: String,
    val created_time: FieldValue,
    val id: String,
    val category:String,
)

data class Author(
    val email: String,
    val id: String,
    val name: String,
)