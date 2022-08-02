package com.yuliamelnyk.app.entity

import java.io.Serializable

//class for save articles in document in FireStore DB
data class ArticleList(
    var articleList: ArrayList<Article> = arrayListOf()
) : Serializable
