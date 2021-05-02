package com.example.icemusic.db

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.internal.synchronized

class MusicDatabaseInstance {
    companion object{
        var any = Any()

        var dbName = "ICE_MUSIC"
        var dbInstance:MusicDatabase? = null

        var MIGRATION_1_2: Migration = object :Migration(1,2){
                override fun migrate(database: SupportSQLiteDatabase) {
                //syntax error:CREATE UNIQUE INDEX songNameIndex ON SearchHistorySong(songName) 空格问题;若字段上已存在重复值，则也会报错
                database.execSQL("create unique index if not exists songNameIndex on SearchHistorySong(songName)")
            }
        }

        fun getInstance(applicationContext: Context):MusicDatabase{
            if(dbInstance==null){
                kotlin.synchronized(any){
                    if(dbInstance==null){
                        dbInstance = Room.databaseBuilder(applicationContext,
                            MusicDatabase::class.java,dbName).addMigrations(MIGRATION_1_2).
                            build()
                    }
                }
            }
            return dbInstance!!
        }
    }
}