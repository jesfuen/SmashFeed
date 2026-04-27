package com.example.smashfeed.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smashfeed.data.local.dao.PostDAO
import com.example.smashfeed.data.local.dao.UserDAO
import com.example.smashfeed.data.local.dao.UserLikeDAO
import com.example.smashfeed.data.local.dao.UserSavedDAO
import com.example.smashfeed.data.local.entity.PostEntity
import com.example.smashfeed.data.local.entity.UserEntity
import com.example.smashfeed.data.local.entity.UserLikeEntity
import com.example.smashfeed.data.local.entity.UserSavedEntity

@Database(entities = [UserEntity::class, PostEntity::class, UserLikeEntity::class, UserSavedEntity::class], version = 7)
abstract class SmashFeedRoomDatabase : RoomDatabase() {
    abstract fun userDAO(): UserDAO
    abstract fun postDAO(): PostDAO
    abstract fun userLikeDAO(): UserLikeDAO
    abstract fun userSavedDAO(): UserSavedDAO

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