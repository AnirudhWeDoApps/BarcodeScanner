package com.wedoapps.barcodescanner.Ui.Report.SingleReport

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.wedoapps.barcodescanner.Adapter.ReportSingleItemAdapter
import com.wedoapps.barcodescanner.BarcodeViewModel
import com.wedoapps.barcodescanner.R
import com.wedoapps.barcodescanner.Ui.Report.SingleReport.BottomSheets.FilterBottomSheet
import com.wedoapps.barcodescanner.Utils.BarcodeApplication
import com.wedoapps.barcodescanner.Utils.Constants.TAG
import com.wedoapps.barcodescanner.Utils.ViewModelProviderFactory
import com.wedoapps.barcodescanner.databinding.ActivitySingleReportBinding
import java.text.SimpleDateFormat
import java.util.*

class SingleReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySingleReportBinding
    private val viewModel: BarcodeViewModel by viewModels {
        ViewModelProviderFactory(
            application,
            (application as BarcodeApplication).repository
        )
    }

    @SuppressLint("SimpleDateFormat")
    var dfDate = SimpleDateFormat("dd/MM/yyyy")

    private lateinit var datePickerTO: DatePickerDialog
    private lateinit var datePickerFROM: DatePickerDialog

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingleReportBinding.inflate(layoutInflater)
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

        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val okColor = ContextCompat.getColor(this, R.color.gd_center)
        val cancelColor = ContextCompat.getColor(this, R.color.gd_center)
        binding.tvStartDate.text = dfDate.format(System.currentTimeMillis())
        binding.tvEndDate.text = dfDate.format(System.currentTimeMillis())

        datePickerFROM =
            DatePickerDialog(
                this,
                R.style.DialogTheme,
                { _, yearDP, monthOfYear, dayOfMonth ->
                    val fmonth = monthOfYear + 1
                    var fm = "" + fmonth
                    var fd = "" + dayOfMonth
                    if (fmonth < 10) {
                        fm = "0$fmonth"
                    }
                    if (dayOfMonth < 10) {
                        fd = "0$dayOfMonth"
                    }
                    val fromDate = "$fd/$fm/$yearDP"
                    binding.tvStartDate.text = fromDate
                },
                year,
                month,
                day
            )

        datePickerTO =
            DatePickerDialog(
                this,
                R.style.DialogTheme,
                { _, yearDP, monthOfYear, dayOfMonth ->
                    val fmonth = monthOfYear + 1
                    var fm = "" + fmonth
                    var fd = "" + dayOfMonth
                    if (fmonth < 10) {
                        fm = "0$fmonth"
                    }
                    if (dayOfMonth < 10) {
                        fd = "0$dayOfMonth"
                    }
                    val toDate = "$fd/$fm/$yearDP"
                    binding.tvEndDate.text = toDate

                },
                year,
                month,
                day
            )
        datePickerTO.datePicker.minDate = calendar.timeInMillis

        binding.tvStartDate.setOnClickListener {
            datePickerFROM.show()
            datePickerFROM.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(okColor)
            datePickerFROM.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(cancelColor)
        }

        binding.tvEndDate.setOnClickListener {
            datePickerTO.show()
            datePickerTO.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(okColor)
            datePickerTO.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(cancelColor)
        }

        binding.btnSearch.setOnClickListener {
            sendDataRequest()
        }
        loadData()

        binding.tvSearch.setOnClickListener {
            showSearch()
        }

        binding.btnClose.setOnClickListener {
            hideSearch()
        }

        binding.tvFilter.setOnClickListener {
            val filter = FilterBottomSheet()
            filter.show(supportFragmentManager, filter.tag)
        }

    }

    private fun hideSearch() {
        binding.apply {
            frameSearch.visibility = View.GONE
            tvSearch.visibility = View.VISIBLE
            tvFilter.visibility = View.VISIBLE
        }
    }

    private fun showSearch() {
        binding.apply {
            tvSearch.visibility = View.GONE
            tvFilter.visibility = View.GONE
            frameSearch.visibility = View.VISIBLE
        }
    }

    private fun hideEmpty() {
        binding.apply {
            ivNoData.visibility = View.GONE
            rvSingleItem.visibility = View.VISIBLE
        }
    }

    private fun showEmpty() {
        binding.apply {
            ivNoData.visibility = View.VISIBLE
            rvSingleItem.visibility = View.GONE
        }
    }

    private fun loadData() {
        val adapterSingleItem = ReportSingleItemAdapter()
        viewModel.singleReportItemLiveData.observe(this) {
            Log.d(TAG, "loadData: $it")
            if (it.isNullOrEmpty()) {
                showEmpty()
            } else {
                hideEmpty()
                adapterSingleItem.differ.submitList(it.sortedByDescending { it1 -> it1.quantity })
                binding.rvSingleItem.apply {
                    setHasFixedSize(true)
                    adapter = adapterSingleItem
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sendDataRequest()
    }

    private fun sendDataRequest() {
        viewModel.singleReportItem(
            binding.tvStartDate.text as String,
            binding.tvEndDate.text as String
        )
    }


}