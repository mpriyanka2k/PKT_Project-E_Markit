package com.pkt.emarkit.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pkt.emarkit.db.dao.devilery_dao
import com.pkt.emarkit.db.data_models.*


@Database(entities = [Delivery::class], version = 1, exportSchema = false)
abstract class AppDatabase:RoomDatabase() {

    abstract fun deliveryDao():devilery_dao


    companion object{
        @Volatile
        private var INSTANCE:AppDatabase? = null
        fun getDatabase(context: Context):AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                              context.applicationContext,
                              AppDatabase::class.java,
                         "emarkit_Database",).build()
                INSTANCE = instance
                instance
            }
        }
    }
}