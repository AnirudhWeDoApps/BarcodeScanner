package com.wedoapps.barcodescanner.Utils

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Constants {

    const val TAG = "BarcodeScanner"
    const val BARCODE = "Barcode"
    const val BARCODE_DATA = "ScannedData"
    const val USER_DATA = "UserData"
    const val SP_NAME = "Sp"
    const val NAME = "Name"
    const val VENDOR_NAME = "VendorName"
    const val DOWNLOAD = 1
    const val SHARE = 0
    const val PDF_DATA = "PdfData"
    const val REPORT_DATA = "ReportData"
    const val IS_NEW = "isNew"
    const val REQUEST_CODE = 1
    const val HISTORY_DATA = "HistoryData"
    const val VENDOR_DATA = "VendorData"
    const val BUYER_DATA = "BuyerData"
    const val BY_MANUAL = "ByManual"
    const val MANUALLY = "Manually"

    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
        }
    }
}