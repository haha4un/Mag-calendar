package com.example.myapplication

import android.app.ProgressDialog.show
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG

class dbhelp {

    //"Create Table if not exists data (data TEXT NOT NULL, description TEXT NOT NULL)"
    fun createDate(db: SQLiteDatabase, date: String, desc: String, table: String)
    {db.execSQL("INSERT INTO $table VALUES ('$date','$desc')")}

    fun delDate(db: SQLiteDatabase, date: String, table: String) {
        var cursor: Cursor = db.rawQuery("SELECT * FROM $table where url = '$date'", null)
        while(cursor.moveToNext())
            if (date == cursor.getString(0))
                db.execSQL("Delete from $table where url = '$date'") }

    fun tableToarr(db: SQLiteDatabase, table: String, which: Int) : Array<String>
    {
        var cursor: Cursor = db.rawQuery("SELECT * FROM $table", null)
        var arr = emptyArray<String>()
        while(cursor.moveToNext()) {
            arr += cursor.getString(which)
        }
        return arr
    }
    fun read(db: SQLiteDatabase, table: String, which: Int, Ofwhat: String): Array<String>
    {
        var cursor: Cursor = db.rawQuery("SELECT * FROM $table where $Ofwhat == $which", null)
        var arr = emptyArray<String>()
        while(cursor.moveToNext()) {
            arr += cursor.getString(which)
        }
        return arr
    }
}