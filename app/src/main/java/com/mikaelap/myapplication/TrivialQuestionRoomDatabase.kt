package com.mikaelap.myapplication

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//calls course dao queries and funcs
@Database(entities = [(TrivialQuestion::class)], version = 2)
abstract class TrivialQuestionRoomDatabase: RoomDatabase() {

    abstract fun TrivialQuestionDao(): TrivialQuestionDao

    companion object {

        private var INSTANCE: TrivialQuestionRoomDatabase? = null

        fun getInstance(context: Context): TrivialQuestionRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TrivialQuestionRoomDatabase::class.java,
                        "question_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}