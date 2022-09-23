package com.example.myapplication

import android.content.Context
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var cal: CalendarView = findViewById(R.id.y)
        // var scroll: LinearLayout = findViewById(R.id.scrooler)

        var database = Firebase.database
        var myRef = database.getReference("date")
        var dates = ArrayList<String>()
        var desriptions = ArrayList<String>()
        var imgs = ArrayList<String>()

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dates.size>0)
//                    dates.clear()
                for (i in dataSnapshot.children)
                {
                    var fbe = i.getValue(fb().javaClass)
                    dates.add(fbe?.getDates().toString())
                    desriptions.add(fbe?.getDescription().toString())
                    imgs.add(fbe?.getImage().toString())

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


        cal.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var m = month + 1
            var key = "$dayOfMonth.$m"
            var scrool: LinearLayout = findViewById(R.id.scrooler)
            scrool.removeAllViews()

            if ( !isOnline(this) ){
                Toast.makeText(this, "Подключитесь к интернету", Toast.LENGTH_SHORT).show();
                return@setOnDateChangeListener
            }

            try {
            getDates("$dayOfMonth-$m", dates, desriptions, imgs, scrool)}
            catch (E: IndexOutOfBoundsException)
            {
                Toast.makeText(this, "Ошибка: $E", Toast.LENGTH_SHORT)
                    .show()
                return@setOnDateChangeListener
            }

            try {
            for (i in m..12) {
                for (k in dayOfMonth..30) {
                    if (getAdvice("$k.$m", dates)) {
                        Toast.makeText(this, "Самое ближнее событие: $k.$m", Toast.LENGTH_SHORT)
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
        key: String,
        dates: ArrayList<String>,
        desc: ArrayList<String>,
        imgs: ArrayList<String>,
        scrool: LinearLayout
    ){
        for (i in 0..dates.size -1) {
            var open = dates[i].dropLastWhile { it != '.' }.dropLast(1)
            if (open == key) {
                var txt: TextView = TextView(this)
                val face = ResourcesCompat.getFont(this, R.font.activist);
                txt.setTypeface(face)
                txt.textSize = 40f


                var str = sentData(dates[i].dropLastWhile { it != '.' }.dropLast(1).dropLastWhile { it != '.' }.dropLast(1).toInt(),
                    dates[i].dropLastWhile { it != '.' }.dropLast(1).dropWhile { it != '.' }.drop(1).toInt(),
                    dates[i].dropWhile { it != '.' }.drop(1).dropWhile { it != '.' }.drop(1).toInt())
                txt.text = "${str}\n${desc[i]}\n\n"
                scrool.addView(txt)
                if (imgs[i] != "null")
                {
                    var img: ImageView = ImageView(this)
                    Picasso.with(this).load(imgs[i]).placeholder(R.drawable.img).error(R.drawable.img).into(img)
                    scrool.addView(img)
                }
            }
        }
    }

    fun sentData(day: Int, mounth: Int, year: Int) : String
    {
        var str =""
        if (day < 10)
            str += "0${day.toString()}."
        else
            str+="$day."

        if(mounth < 10)
            str += "0${mounth.toString()}."
        else
            str+="$mounth."

        str+= year.toString()

        return str
    }

    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = null
        activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
    }
}
