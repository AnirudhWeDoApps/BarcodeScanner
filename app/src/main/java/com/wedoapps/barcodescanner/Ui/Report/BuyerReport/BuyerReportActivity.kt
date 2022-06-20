package com.wedoapps.barcodescanner.Ui.Report.BuyerReport

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.wedoapps.barcodescanner.Adapter.BuyerReportAdapter
import com.wedoapps.barcodescanner.BarcodeViewModel
import com.wedoapps.barcodescanner.Model.BuyerReportModal
import com.wedoapps.barcodescanner.Utils.BarcodeApplication
import com.wedoapps.barcodescanner.Utils.Constants.BUYER_DATA
import com.wedoapps.barcodescanner.Utils.ViewModelProviderFactory
import com.wedoapps.barcodescanner.databinding.ActivityBuyerReportBinding


class BuyerReportActivity : AppCompatActivity(), BuyerReportAdapter.OnBuyerClick {

    private lateinit var binding: ActivityBuyerReportBinding
    private val viewModel: BarcodeViewModel by viewModels {
        ViewModelProviderFactory(
            application,
            (application as BarcodeApplication).repository
        )
    }

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

        viewModel.buyerReport()

        val buyerAdapter = BuyerReportAdapter(this)
        viewModel.buyerReportLiveData.observe(this) {
            if (it.isNullOrEmpty()) {
                showEmpty()
            } else {
                hideEmpty()
                buyerAdapter.differ.submitList(it.sortedByDescending { list -> list.total })
                binding.rvBuyer.apply {
                    setHasFixedSize(true)
                    adapter = buyerAdapter
                }
            }
        }
    }

    private fun hideEmpty() {
        binding.apply {
            ivNoData.visibility = View.GONE
            rvBuyer.visibility = View.VISIBLE
        }
    }

    private fun showEmpty() {
        binding.apply {
            ivNoData.visibility = View.VISIBLE
            rvBuyer.visibility = View.GONE
        }
    }

    override fun onClick(buyerReportModal: BuyerReportModal) {
        val intent = Intent(this, ChartBuyerReportActivity::class.java)
        intent.putExtra(BUYER_DATA, buyerReportModal)
        startActivity(intent)
    }


}