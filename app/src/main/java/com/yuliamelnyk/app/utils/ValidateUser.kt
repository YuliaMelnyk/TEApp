package com.yuliamelnyk.app.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

//class for validate email and password
class ValidateUser {
    fun isEmailValid(email: String): Boolean {
        var ePattern =
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
        var p: Pattern = Pattern.compile(ePattern)
        var m: Matcher = p.matcher(email)
        return m.matches()
    }

    fun isPasswordValid(password: String): Boolean {
        return password.length > 6
    }
}