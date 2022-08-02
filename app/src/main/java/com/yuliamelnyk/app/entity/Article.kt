package com.yuliamelnyk.app.entity

import com.yuliamelnyk.app.R
import java.io.Serializable
import java.time.LocalDate

data class Article(
    var title: String,
    var text: String,
    var category: List<String>,
    var date: LocalDate,
    var author: User,
    var imgLikes: Int = R.drawable.heart,
    var txtLikes: Int,
    var imgBookmarks: Int = R.drawable.bookmark,
    var txtBookmarks: Int,
    var imgComment: Int = R.drawable.comment,
    var comments: List<Comment>?,
    var image: Int?
) : Serializable