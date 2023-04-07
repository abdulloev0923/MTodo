package com.example.myapplicationtodo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Todo::class], version = 1
)
abstract class TodoDatabase: RoomDatabase() {

    abstract fun getTodoDao():Dao

    companion object{

        @Volatile
        private var instance:TodoDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance?: synchronized(this){
            instance?:buildDatabase(context).also{
                instance = it
            }
        }

        private fun buildDatabase(context: Context): TodoDatabase {

            return Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                "app_database"
            ).build()
        }

    }

}
