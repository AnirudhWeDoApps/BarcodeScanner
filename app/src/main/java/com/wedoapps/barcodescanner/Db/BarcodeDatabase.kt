package com.wedoapps.barcodescanner.Db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.wedoapps.barcodescanner.Model.BarcodeEntryItem
import com.wedoapps.barcodescanner.Model.PDFData
import com.wedoapps.barcodescanner.Model.ScannedData
import com.wedoapps.barcodescanner.Model.Users
import com.wedoapps.barcodescanner.Utils.Convertor

@Database(
    entities = [ScannedData::class, BarcodeEntryItem::class, Users::class, PDFData::class],
    version = 1,
    exportSchema = true
)

@TypeConverters(Convertor::class)
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