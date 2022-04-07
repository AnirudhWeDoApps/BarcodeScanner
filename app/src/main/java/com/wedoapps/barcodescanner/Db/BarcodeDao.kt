package com.wedoapps.barcodescanner.Db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wedoapps.barcodescanner.Model.BarcodeEntryItem
import com.wedoapps.barcodescanner.Model.ScannedData
import com.wedoapps.barcodescanner.Model.Users

@Dao
interface BarcodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(data: ScannedData)

    @Update
    suspend fun updateItem(data: ScannedData)

    @Query("SELECT * FROM ScannedData")
    fun getAllItems(): LiveData<List<ScannedData>>

    @Query("SELECT * FROM ScannedData")
    suspend fun getAllItemsWithoutObservers(): List<ScannedData>

    @Query("SELECT * FROM ScannedData where barcodeNumber = :barcodeNumber")
    suspend fun getScannedDataWO(barcodeNumber: String): ScannedData

    @Delete
    suspend fun deleteItem(data: ScannedData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBarcodeData(item: BarcodeEntryItem)

    @Update
    suspend fun updateBarcodeData(item: BarcodeEntryItem)

    @Delete
    suspend fun deleteBarcodeData(item: BarcodeEntryItem)

    @Query("SELECT * FROM BarcodeEntryItem")
    fun getAllBarcodeDataList(): LiveData<List<BarcodeEntryItem>>

    @Query("SELECT * FROM BarcodeEntryItem where barcodeNumber = :barcodeNumber")
    suspend fun getBarcodeDataWO(barcodeNumber: String): BarcodeEntryItem


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: Users)

    @Update
    suspend fun updateUser(user: Users)

    @Delete
    suspend fun deleteUser(user: Users)

    @Query("SELECT * FROM users")
    fun getAllUserList(): LiveData<List<Users>>

    @Query("DELETE FROM scanneddata")
    suspend fun deleteScannedData()
}
