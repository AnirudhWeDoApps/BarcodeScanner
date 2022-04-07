package com.wedoapps.barcodescanner.Repository

import com.wedoapps.barcodescanner.Db.BarcodeDatabase
import com.wedoapps.barcodescanner.Model.BarcodeEntryItem
import com.wedoapps.barcodescanner.Model.ScannedData
import com.wedoapps.barcodescanner.Model.Users


class BarcodeRepository(private val db: BarcodeDatabase) {

    suspend fun insertItem(data: ScannedData) = db.getBarcodeDao().insertItem(data)

    suspend fun updateItem(data: ScannedData) = db.getBarcodeDao().updateItem(data)

    suspend fun deleteItem(data: ScannedData) = db.getBarcodeDao().deleteItem(data)

    fun getAllItems() = db.getBarcodeDao().getAllItems()

    suspend fun getAllItemsWithoutObservers() = db.getBarcodeDao().getAllItemsWithoutObservers()

    suspend fun insertBarcodeData(item: BarcodeEntryItem) =
        db.getBarcodeDao().insertBarcodeData(item)

    suspend fun updateBarcodeData(item: BarcodeEntryItem) =
        db.getBarcodeDao().updateBarcodeData(item)

    suspend fun deleteBarcodeData(item: BarcodeEntryItem) =
        db.getBarcodeDao().deleteBarcodeData(item)

    suspend fun getBarcodeCodeWO(barcodeNumber: String) =
        db.getBarcodeDao().getBarcodeDataWO(barcodeNumber)

    suspend fun getScannedDataWO(barcodeNumber: String) =
        db.getBarcodeDao().getScannedDataWO(barcodeNumber)

    fun getBarcodeDataList() = db.getBarcodeDao().getAllBarcodeDataList()

    suspend fun insertUser(users: Users) = db.getBarcodeDao().addUser(users)

    suspend fun updateUser(users: Users) = db.getBarcodeDao().updateUser(users)

    suspend fun deleteUser(users: Users) = db.getBarcodeDao().deleteUser(users)

    fun getAllUserList() = db.getBarcodeDao().getAllUserList()

    suspend fun deleteScannedData() = db.getBarcodeDao().deleteScannedData()
}
