package com.wedoapps.barcodescanner

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wedoapps.barcodescanner.Model.BarcodeEntryItem
import com.wedoapps.barcodescanner.Model.PDFData
import com.wedoapps.barcodescanner.Model.ScannedData
import com.wedoapps.barcodescanner.Model.Users
import com.wedoapps.barcodescanner.Repository.BarcodeRepository
import com.wedoapps.barcodescanner.Utils.Constants.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BarcodeViewModel(
    app: Application,
    private val repository: BarcodeRepository
) : AndroidViewModel(app) {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1500L)
            _isLoading.value = false
        }
    }

    private val _historyDataMutableLiveData = MutableLiveData<List<PDFData>>()
    val historyDataLiveData: LiveData<List<PDFData>>
        get() = _historyDataMutableLiveData

    val barcodeDataMutableLiveData = MutableLiveData<BarcodeEntryItem?>()
    val scannedDataInsertAndUpdateResponseLiveData = MutableLiveData<String>()
    private var itemList = mutableListOf<ScannedData>()

    private fun insertScannedItem(
        barcodeNumber: String,
        item: String,
        price: Int,
        originalPrice: Int?,
        storeQuantity: Int?,
        showDialog: Boolean?,
        minCount: Int?,
    ) = viewModelScope.launch {
        val scannedData = ScannedData(
            null,
            barcodeNumber,
            item,
            price,
            originalPrice,
            storeQuantity,
            minCount,
            showDialog,
            1
        )
        repository.insertItem(scannedData)
    }

    fun updateScannedData(
        id: Int,
        barcodeNumber: String,
        item: String,
        price: Int?,
        originalPrice: Int?,
        storeQuantity: Int?,
        minCount: Int?,
        showDialog: Boolean?,
        count: Int?
    ) = viewModelScope.launch {
        val scannedData = ScannedData(
            id,
            barcodeNumber,
            item,
            price,
            originalPrice,
            storeQuantity,
            minCount,
            showDialog,
            count
        )
        repository.updateItem(scannedData)
    }

    fun deleteScannedData(scannedData: ScannedData) = viewModelScope.launch {
        repository.deleteItem(scannedData)
    }

    fun addBarcodeItem(
        barcodeNumber: String,
        itemCode: String,
        item: String,
        sellingPrice: Int,
        purchasePrice: Int,
        count: Int,
        minCount: Int?
    ) = viewModelScope.launch {
        val barcodeEntryItem = BarcodeEntryItem(
            null,
            barcodeNumber,
            itemCode,
            item,
            sellingPrice,
            purchasePrice,
            count,
            minCount
        )

        repository.insertBarcodeData(barcodeEntryItem)
    }

    fun updateBarcodeItem(
        id: Int?,
        barcodeNumber: String,
        itemCode: String,
        item: String,
        sellingPrice: Int?,
        purchasePrice: Int?,
        count: Int?,
        minCount: Int?
    ) = viewModelScope.launch {
        val barcodeEntryItem = BarcodeEntryItem(
            id,
            barcodeNumber,
            itemCode,
            item,
            sellingPrice,
            purchasePrice,
            count,
            minCount
        )
        repository.updateBarcodeData(barcodeEntryItem)
    }

    fun deleteBarcodeItem(barcodeEntryItem: BarcodeEntryItem) = viewModelScope.launch {
        repository.deleteBarcodeData(barcodeEntryItem)
    }

    fun getScannedDataList() = repository.getAllItems()

    fun getBarcodeDataList() = repository.getBarcodeDataList()

    fun getSingleBarcodeData(barcodeNumber: String) = viewModelScope.launch {
        handleBarcodeItemEntry(barcodeNumber)
    }

    private suspend fun handleBarcodeItemEntry(barcodeNumber: String) {
        val barcodeEntryList = repository.getBarcodeCodeWO(barcodeNumber)

        if (barcodeEntryList == null) {
            barcodeDataMutableLiveData.postValue(
                BarcodeEntryItem(
                    null,
                    barcodeNumber,
                    "",
                    "",
                    null,
                    null,
                    null,
                    null
                )
            )
        } else if (barcodeEntryList.barcodeNumber == barcodeNumber) {
            insertAndUpdateScannedData(
                barcodeEntryList.barcodeNumber.toString(),
                barcodeEntryList.itemName.toString(),
                barcodeEntryList.sellingPrice!!,
                barcodeEntryList.count!!,
                barcodeEntryList.minQuantity!!,
                barcodeEntryList.sellingPrice!!,
                true
            )
            barcodeDataMutableLiveData.postValue(barcodeEntryList)
        }
    }

    fun insertAndUpdateScannedData(
        barcodeNumber: String,
        item: String,
        price: Int,
        storeQuantity: Int?,
        minCount: Int?,
        originalPrice: Int?,
        showDialog: Boolean?
    ) =
        viewModelScope.launch {
            handleInsertAndUpdateScannedData(
                barcodeNumber,
                item,
                price,
                storeQuantity,
                minCount,
                originalPrice,
                showDialog
            )
        }

    private suspend fun handleInsertAndUpdateScannedData(
        barcodeNumber: String?,
        item: String,
        price: Int,
        storeQuantity: Int?,
        minCount: Int?,
        originalPrice: Int?,
        showDialog: Boolean?
    ) {
        itemList = repository.getAllItemsWithoutObservers().toMutableList()
        val foundItem = itemList.find { fItem -> fItem.barcodeNumber.equals(barcodeNumber) }
        var isUpdated = 0       // 0 == NotUpdated,  1 == Updated
        if (foundItem != null) {
            val totalPrice = foundItem.price?.plus(price)
            val totalCount = foundItem.count?.plus(1)
            isUpdated = 1
            updateScannedData(
                id = foundItem.id!!,
                barcodeNumber = barcodeNumber ?: foundItem.barcodeNumber.toString(),
                item = foundItem.item.toString(),
                price = if (showDialog == true) totalPrice else foundItem.price,
                originalPrice = originalPrice ?: foundItem.originalPrice,
                storeQuantity = storeQuantity ?: foundItem.storeQuantity,
                minCount = foundItem.minCount,
                showDialog = showDialog,
                count = if (showDialog == true) totalCount else foundItem.count
            )
            scannedDataInsertAndUpdateResponseLiveData.postValue("$item Updated")
        }

        if (isUpdated == 0) {
            insertScannedItem(
                barcodeNumber.toString(),
                item,
                price,
                originalPrice = price,
                storeQuantity,
                showDialog,
                minCount
            )
            scannedDataInsertAndUpdateResponseLiveData.postValue("$item Added")
        }
    }

    fun addUser(
        name: String, mobileNumber: String,
        address1: String, city: String, pincode: String
    ) = viewModelScope.launch {
        val user = Users(
            null,
            name,
            mobileNumber,
            address1,
            city,
            pincode
        )
        repository.insertUser(user)
    }

    fun updateUser(
        id: Int?, name: String, mobileNumber: String,
        address1: String, address2: String, pincode: String
    ) = viewModelScope.launch {
        val user = Users(
            id,
            name,
            mobileNumber,
            address1,
            address2,
            pincode
        )
        repository.updateUser(user)
    }

    fun deleteUser(user: Users) = viewModelScope.launch {
        repository.deleteUser(user)
    }

    fun getUserList() = repository.getAllUserList()

    fun deleteScannedData() = viewModelScope.launch {
        repository.deleteScannedData()
    }

    fun removeStocks(barcodeNumber: MutableList<String?>) = viewModelScope.launch {
        safeHandleRemoveStocks(barcodeNumber)
    }

    private suspend fun safeHandleRemoveStocks(barcodeNumber: MutableList<String?>) {
        if (barcodeNumber.isNotEmpty()) {
            for (i in barcodeNumber) {
                val findBarcodeData = repository.getBarcodeCodeWO(i.toString())
                val findScannedData = repository.getScannedDataWO(i.toString())

                Log.d(
                    TAG,
                    "safeHandleRemoveStocks: ${findBarcodeData.barcodeNumber} ${findScannedData.barcodeNumber}"
                )

                if (findBarcodeData.barcodeNumber.equals(findScannedData.barcodeNumber)) {
                    val newQuantity = findBarcodeData.count?.minus(findScannedData.count ?: 0)
                    Log.d(TAG, "safeHandleRemoveStocks: $newQuantity")
                    updateBarcodeItem(
                        findBarcodeData.id,
                        findBarcodeData.barcodeNumber.toString(),
                        findBarcodeData.itemCode.toString(),
                        findBarcodeData.itemName.toString(),
                        findBarcodeData.sellingPrice,
                        findBarcodeData.purchasePrice,
                        newQuantity,
                        findBarcodeData.minQuantity
                    )
                    insertAndUpdateScannedData(
                        findBarcodeData.barcodeNumber.toString(),
                        findBarcodeData.itemName.toString(),
                        findBarcodeData.sellingPrice!!,
                        newQuantity,
                        findBarcodeData.minQuantity,
                        findBarcodeData.purchasePrice,
                        false
                    )
                }

                if (findScannedData.storeQuantity!! <= 0) {
                    deleteScannedData(findScannedData)
                }
            }
        }
    }

    fun updateScannedItem(
        barcodeNumber: String,
        item: String,
        originalPrice: Int?,
        storeQuantity: Int?,
    ) = viewModelScope.launch {
        handleUpdate(
            barcodeNumber, item, originalPrice, storeQuantity
        )
    }

    private suspend fun handleUpdate(
        barcodeNumber: String,
        item: String,
        originalPrice: Int?,
        storeQuantity: Int?,
    ) {
        val scannedList = repository.getAllItemsWithoutObservers()
        val foundItem = scannedList.find { it.barcodeNumber == barcodeNumber }

        if (foundItem != null) {
            updateScannedData(
                foundItem.id!!,
                barcodeNumber,
                item,
                foundItem.price,
                originalPrice,
                storeQuantity,
                foundItem.minCount,
                foundItem.showDialog,
                foundItem.count
            )
        }
    }

    fun addHistoryItem(
        name: String,
        itemList: ArrayList<ScannedData>?,
        phoneNumber: String,
        total: String,
        date: String,
        time: String
    ) = viewModelScope.launch {
        val pdfData = PDFData(
            null, name, itemList, phoneNumber, total, date, time
        )
        repository.addHistoryItem(pdfData)
    }

    fun deleteHistoryItem(pdfData: PDFData) = viewModelScope.launch {
        repository.deleteHistoryItem(pdfData)
    }

    fun getHistoryList() = repository.getAllHistoryList()

    fun historyDateWise(toDate: String, fromDate: String) = viewModelScope.launch {
        handleHistoryDateWise(toDate, fromDate)
    }

    private suspend fun handleHistoryDateWise(toDate: String, fromDate: String) {
        Log.d(TAG, "view: $toDate $fromDate")
        val list = repository.getHistoryDate(toDate, fromDate)
        Log.d(TAG, "view LIST: $list")
        _historyDataMutableLiveData.postValue(list)
    }


}