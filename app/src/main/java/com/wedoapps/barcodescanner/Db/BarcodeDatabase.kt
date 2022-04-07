package com.wedoapps.barcodescanner.Db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wedoapps.barcodescanner.Model.BarcodeEntryItem
import com.wedoapps.barcodescanner.Model.ScannedData
import com.wedoapps.barcodescanner.Model.Users

@Database(
    entities = [ScannedData::class, BarcodeEntryItem::class, Users::class],
    version = 2,
)

abstract class BarcodeDatabase : RoomDatabase() {

    abstract fun getBarcodeDao(): BarcodeDao

    companion object {

        @Volatile
        private var instance: BarcodeDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }


        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                BarcodeDatabase::class.java,
                "Barcode.db"
            ).fallbackToDestructiveMigration()
                .build()
    }

}