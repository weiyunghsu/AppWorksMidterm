package com.weiyung.publisher

data class Content(
    val author: List<Author>,
    val title: String,
    val content: String,
    val createdTime: Long = System.currentTimeMillis(),
    val id: String,
    val category:String,
)

data class Author(
    val email: String,
    val id: String,
    val name: String,
)