package com.example.myapplication

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.CalendarView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var cal: CalendarView = findViewById(R.id.y)
        // var scroll: LinearLayout = findViewById(R.id.scrooler)
        var txt: TextView = findViewById(R.id.txt)

        var hp: dbhelp = dbhelp()
        var bd: SQLiteDatabase = baseContext.openOrCreateDatabase("dates", MODE_PRIVATE, null)
        //bd.execSQL("DROP TABLE data")
        bd.execSQL("Create Table if not exists data (data TEXT NOT NULL, description TEXT NOT NULL)")


        val pm = packageManager
        val pi = pm.getPackageInfo(packageName, 0)
        if (pi != null) {
            bd.execSQL("Create Table if not exists data (data TEXT NOT NULL, description TEXT NOT NULL)")
        }



        hp.createDate(bd, "20.9.2022", "ооо, Спидран по Календарю!", "data")

        cal.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var m = month + 1
            var key = "$dayOfMonth.$m"
            var scrool: LinearLayout = findViewById(R.id.scrooler)

            getDates(txt, "$dayOfMonth.$m")
            for (i in m..12) {
                for (k in dayOfMonth..30) {
                    if (getAdvice(txt, "$k.$m")) {
                        Toast.makeText(this, "самое ближнее событие: $k.$m", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnDateChangeListener
                    }
                }
            }
        }
    }

    fun getAdvice(
        txt: TextView,
        key: String
    ): Boolean {
        var hp: dbhelp = dbhelp()
        var bd: SQLiteDatabase = baseContext.openOrCreateDatabase("dates", MODE_PRIVATE, null)

        var arr = hp.tableToarr(bd, "data",  0)
        for (i in 0..arr.size - 1) {
            var open = arr[i].dropLastWhile { it != '.' }.dropLast(1)
            if (open == key)
                return true
        }
        return false
    }

    fun getDates(
        txt: TextView,
        key: String
    ){
        var hp: dbhelp = dbhelp()
        var bd: SQLiteDatabase = baseContext.openOrCreateDatabase("dates", MODE_PRIVATE, null)

        var arr = hp.tableToarr(bd, "data",  0)
        var a2rr = hp.tableToarr(bd, "data",  1)
        var str = ""
        for (i in 0..arr.size - 1) {
            var open = arr[i].dropLastWhile { it != '.' }.dropLast(1)
            if (open == key)
                str+= "${arr[i]}\n${a2rr[i]}\n\n"
        }
        txt.text = str
    }
}