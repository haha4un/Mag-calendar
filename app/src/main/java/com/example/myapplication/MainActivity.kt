package com.example.myapplication

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.Error


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var cal: CalendarView = findViewById(R.id.y)
        // var scroll: LinearLayout = findViewById(R.id.scrooler)
        var txt: TextView = findViewById(R.id.txt)

        var database = Firebase.database
        var myRef = database.getReference("data")
        var dates = ArrayList<String>()
        var desriptions = ArrayList<String>()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dates.size>0)
//                    dates.clear()
                for (i in dataSnapshot.children)
                {
                    var fbe = i.getValue(fb().javaClass)
                    dates.add(fbe?.getDates().toString())
                    desriptions.add(fbe?.getDescription().toString())

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


        cal.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var m = month + 1
            var key = "$dayOfMonth.$m"
            var scrool: LinearLayout = findViewById(R.id.scrooler)

            try {
            getDates(txt, "$dayOfMonth-$m", dates, desriptions)}
            catch (E: IndexOutOfBoundsException)
            {
                Toast.makeText(this, "ошибка: $E", Toast.LENGTH_SHORT)
                    .show()
                return@setOnDateChangeListener
            }

            try {
            for (i in m..12) {
                for (k in dayOfMonth..30) {
                    if (getAdvice("$k-$m", dates)) {
                        Toast.makeText(this, "самое ближнее событие: $k.$m", Toast.LENGTH_SHORT)
                            .show()
                        return@setOnDateChangeListener
                    }
                }
            }
            }
            catch (E: IndexOutOfBoundsException){
                return@setOnDateChangeListener
            }
        }
    }

    fun getAdvice(
        key: String,
        list: ArrayList<String>
    ): Boolean {
        for (i in 0..list.size-1) {
            var open = list[i].dropLastWhile { it != '-' }.dropLast(1)
            if (open == key)
                return true
        }
        return false
    }

    fun getDates(
        txt: TextView,
        key: String,
        dates: ArrayList<String>,
        desc: ArrayList<String>,
        scroller: LinearLayout
    ){
        var str=""
        for (i in 0..dates.size -1) {
            var open = dates[i].dropLastWhile { it != '-' }.dropLast(1)
            if (open == key)
                str+= "${dates[i]}\n${desc[i]}\n\n"
        }
        txt.text = str
    }
}
