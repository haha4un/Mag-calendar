package com.example.myapplication

class fb {
    var date: String? = null
    var desc: String? = null
    var img: String? = null

    fun fb(
        date: String?,
        desc: String?,
        img: String?,
    ) {
        this.date = date
        this.desc = desc
        this.img = img
    }

    fun getDates(): String? {
        return date
    }

    fun getDescription(): String? {
        return desc
    }
    fun getImage(): String? {
        return img
    }
}