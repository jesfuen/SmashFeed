package com.example.smashfeed.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smashfeed.data.local.dao.PostDAO
import com.example.smashfeed.data.local.dao.UserDAO
import com.example.smashfeed.data.local.entity.PostEntity
import com.example.smashfeed.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, PostEntity::class], version = 6)
abstract class SmashFeedRoomDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun postDAO(): PostDAO

    companion object{
        @Volatile
        private var INSTANCE: SmashFeedRoomDatabase? = null

        fun getInstance(context: Context): SmashFeedRoomDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SmashFeedRoomDatabase::class.java, "smashfeed_database"
                ).allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}