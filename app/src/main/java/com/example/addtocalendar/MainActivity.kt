package com.example.addtocalendar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.addtocalendar.R
import com.example.myapplication.fb
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var database = FirebaseDatabase.getInstance().getReference("date")

        var data: EditText = findViewById(R.id.data)
        var desc: EditText = findViewById(R.id.desc)
        var url: EditText = findViewById(R.id.url)
        var button: Button = findViewById(R.id.btn_add)

        button.setOnClickListener()
        {
            if (data.text.toString() == "" || desc.text.toString() == "") {
                Toast.makeText(this, "STATUS: error", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            var database = FirebaseDatabase.getInstance().getReference("date")
//            var id: String ?= database.key
            var date: String = data.text.toString()
            var desc: String = desc.text.toString()
            var urls: String = url.text.toString()
            if (url.text.toString() == "")
                urls = "null"

            var firebase: fb = fb()
            firebase.fb(date, desc, urls)

            database.push().setValue(firebase)

            Toast.makeText(this, "STATUS: ok", Toast.LENGTH_LONG).show()
        }
    }
}