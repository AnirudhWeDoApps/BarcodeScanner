package com.wedoapps.barcodescanner.Ui.Report.BuyerReport

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wedoapps.barcodescanner.databinding.ActivityBuyerReportBinding


class BuyerReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBuyerReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuyerReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = binding.toolbar.customToolbar
        setSupportActionBar(toolbar)

        binding.toolbar.apply {
            ivSearch.visibility = View.GONE
            ivSetting.visibility = View.GONE
            ivInsertItem.visibility = View.GONE
            ivBack.visibility = View.VISIBLE
            ivUserAdd.visibility = View.GONE
        }

        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}