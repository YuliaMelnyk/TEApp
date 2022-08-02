package com.yuliamelnyk.app.entity

import java.io.Serializable

class User: Serializable {
    lateinit var id: String
    lateinit var name: String
    lateinit var email: String
    lateinit var password: String
    lateinit var article: String

}