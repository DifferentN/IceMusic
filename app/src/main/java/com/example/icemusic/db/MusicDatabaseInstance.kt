package com.example.icemusic.db

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.internal.synchronized

class MusicDatabaseInstance {
    companion object{
        var any = Any()

        var dbName = "ICE_MUSIC"
        var dbInstance:MusicDatabase? = null

        fun getInstance(applicationContext: Context):MusicDatabase{
            if(dbInstance==null){
                kotlin.synchronized(any){
                    if(dbInstance==null){
                        dbInstance = Room.databaseBuilder(applicationContext,
                            MusicDatabase::class.java,dbName).build()
                    }
                }
            }
            return dbInstance!!
        }
    }
}