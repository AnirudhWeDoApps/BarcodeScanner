package com.wedoapps.barcodescanner.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Token(
    val app_id: String,
    val created_at: String,
    val model_name: String,
    val os: String,
    val os_version: String,
    val push_token: String
): Parcelable