package com.example.githubberm.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [Person::class],
    version = 1
)
abstract class PersonDB : RoomDatabase(){

    abstract fun personDao() : PersonDao

    companion object {

        @Volatile private var instance : PersonDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            PersonDB::class.java,
            "person_database.db"
        ).build()

    }
}