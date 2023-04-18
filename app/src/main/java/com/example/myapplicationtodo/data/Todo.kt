package com.example.myapplicationtodo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.time.LocalDateTime


@Entity(tableName = "todo_table")
data class Todo(
    @ColumnInfo(name = "title") var title:String = "",
    @ColumnInfo(name = "description") var description:String = "",
    @ColumnInfo(name = "priority") var priority:Int = 0,
    @ColumnInfo(name = "date") var date: String= ""
):Serializable{
    @PrimaryKey(autoGenerate = true) var id:Int = 0

}