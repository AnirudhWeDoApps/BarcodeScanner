package com.wedoapps.barcodescanner.Model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class PDFData(
    var name: String? = "",
    var cartList: ArrayList<ScannedData>? = arrayListOf(),
    var phoneNumber: String? = "",
    var total: String? = "",
    var date: String = getDateAndTime()
) : Parcelable {
    companion object {
        @SuppressLint("SimpleDateFormat")
        private fun getDateAndTime(): String {
            val currentTime: Date = Calendar.getInstance().time
            val simpleFormat = SimpleDateFormat("dd MMM yyyy hh:mm")
            return simpleFormat.format(currentTime)
        }
    }
}
