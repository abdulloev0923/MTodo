package com.example.myapplicationtodo.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime


@Entity(tableName = "todo_table")
data class Todo(
    @ColumnInfo(name = "title") var title:String = "",
    @ColumnInfo(name = "description") var description:String = "",
    @ColumnInfo(name = "priority") var priority:Int = 0,
    @ColumnInfo(name = "date") var date:String = ""
):Serializable{
    @PrimaryKey(autoGenerate = true) var id:Int = 0

}