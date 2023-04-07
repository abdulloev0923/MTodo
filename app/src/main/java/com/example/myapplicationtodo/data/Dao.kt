package com.example.myapplicationtodo.data

import android.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import kotlinx.coroutines.flow.Flow


@Dao
interface Dao {

    @Insert
    suspend fun addTodo(todo: Todo)
    @Query("SELECT * FROM todo_table")
    suspend fun getAllTodo():List<Todo>
    @Update
    suspend fun updateTodo(todo: Todo)
    @Delete
    suspend fun deleteTodo(todo: Todo)
    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTodo()
    @Query("SELECT * FROM todo_table WHERE title LIKE:searchView")
    suspend fun search(searchView: String):List<Todo>



}