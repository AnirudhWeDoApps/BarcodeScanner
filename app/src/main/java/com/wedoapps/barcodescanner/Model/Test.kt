package com.wedoapps.barcodescanner.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Test(
    val message: String,
    val status: Boolean,
    val token: Token
): Parcelable