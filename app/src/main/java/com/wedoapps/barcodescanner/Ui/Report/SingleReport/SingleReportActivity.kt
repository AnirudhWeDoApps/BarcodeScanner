package com.wedoapps.barcodescanner.Ui.Report.SingleReport

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.wedoapps.barcodescanner.Adapter.ReportSingleItemAdapter
import com.wedoapps.barcodescanner.BarcodeViewModel
import com.wedoapps.barcodescanner.Model.SingleReportModel
import com.wedoapps.barcodescanner.R
import com.wedoapps.barcodescanner.Ui.Report.SingleReport.BottomSheets.FilterBottomSheet
import com.wedoapps.barcodescanner.Utils.BarcodeApplication
import com.wedoapps.barcodescanner.Utils.Constants.ALL
import com.wedoapps.barcodescanner.Utils.Constants.DATE_RANGE
import com.wedoapps.barcodescanner.Utils.Constants.LAST_MONTH
import com.wedoapps.barcodescanner.Utils.Constants.LAST_WEEK
import com.wedoapps.barcodescanner.Utils.Constants.TAG
import com.wedoapps.barcodescanner.Utils.Constants.THIS_DAY
import com.wedoapps.barcodescanner.Utils.Constants.THIS_MONTH
import com.wedoapps.barcodescanner.Utils.ViewModelProviderFactory
import com.wedoapps.barcodescanner.databinding.ActivitySingleReportBinding
import java.text.SimpleDateFormat
import java.util.*

class SingleReportActivity : AppCompatActivity(), FilterBottomSheet.OnFilterOptionSelected {

    private lateinit var binding: ActivitySingleReportBinding
    private val viewModel: BarcodeViewModel by viewModels {
        ViewModelProviderFactory(
            application,
            (application as BarcodeApplication).repository
        )
    }

    @SuppressLint("SimpleDateFormat")
    var dfDate = SimpleDateFormat("dd/MM/yyyy")
    private lateinit var adapterSingleItem: ReportSingleItemAdapter
    private lateinit var itemList: ArrayList<SingleReportModel>
    private lateinit var datePickerTO: DatePickerDialog
    private lateinit var datePickerFROM: DatePickerDialog
    private var fromDate: String? = null ?: ""
    private var toDate: String? = null ?: ""

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
        datePickerFROM.setTitle("Start Date")

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
        datePickerTO.setTitle("End Date")
        datePickerTO.datePicker.minDate = calendar.timeInMillis

        binding.tvStartDate.setOnClickListener {
            val cancelColor = ContextCompat.getColor(this, R.color.gd_center)
            val okColor = ContextCompat.getColor(this, R.color.gd_center)
            datePickerFROM.show()
            datePickerFROM.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(okColor)
            datePickerFROM.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(cancelColor)
            datePickerFROM.getButton(DatePickerDialog.BUTTON_POSITIVE).setOnClickListener {
                datePickerFROM.cancel()
            }
        }

        binding.tvEndDate.setOnClickListener {
            val cancelColor = ContextCompat.getColor(this, R.color.gd_center)
            val okColor = ContextCompat.getColor(this, R.color.gd_center)
            datePickerTO.show()
            datePickerTO.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(okColor)
            datePickerTO.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(cancelColor)
            datePickerTO.getButton(DatePickerDialog.BUTTON_POSITIVE).setOnClickListener {
                sendDataRequest(DATE_RANGE)
                datePickerTO.cancel()
            }
        }

        binding.btnSearch.setOnClickListener {
            sendDataRequest(DATE_RANGE)
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

        binding.edtSearch.addTextChangedListener {
            filter(it.toString())
        }

    }

    private fun hideSearch() {
        binding.apply {
            frameSearch.visibility = View.GONE
            tvSearch.visibility = View.VISIBLE
            tvFilter.visibility = View.VISIBLE
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

    private fun filter(text: String) {
        val filteredList: ArrayList<SingleReportModel> = ArrayList()
        for (item in itemList) {
            if (item.itemName?.lowercase(Locale.getDefault())!!
                    .contains(text.lowercase(Locale.getDefault()))
            ) {
                hideEmpty()
                filteredList.add(item)
            } else {
                showEmpty()
            }
        }
        adapterSingleItem.filterList(filteredList)
    }

    private fun loadData() {
        adapterSingleItem = ReportSingleItemAdapter(arrayListOf())
        viewModel.singleReportItemLiveData.observe(this) {
            Log.d(TAG, "loadData: $it")
            if (it.isNullOrEmpty()) {
                showEmpty()
            } else {
                hideEmpty()
                itemList =
                    it as ArrayList<SingleReportModel>
                adapterSingleItem.filterList(itemList)
                binding.rvSingleItem.apply {
                    setHasFixedSize(true)
                    adapter = adapterSingleItem
                }
                Log.d(TAG, "loadData: $it")
            }
        }

    }

    private fun showSearch() {
        binding.apply {
            tvSearch.visibility = View.GONE
            tvFilter.visibility = View.GONE
            frameSearch.visibility = View.VISIBLE
            edtSearch.requestFocus()
        }
    }

    override fun onResume() {
        super.onResume()
        sendDataRequest(DATE_RANGE)
    }

    private fun sendDataRequest(type: String) {
        viewModel.singleReportItem(
            type,
            fromDate.toString(),
            toDate.toString()
        )

        Log.d(TAG, "sendDataRequest: $type, $fromDate, $toDate")
    }

    override fun onSelect(type: String) {
        when (type) {
            ALL -> {
                sendDataRequest(ALL)
                binding.tvFilter.text = ALL
            }
            THIS_MONTH -> {
                sendDataRequest(THIS_MONTH)
                binding.tvFilter.text = THIS_MONTH
            }
            THIS_DAY -> {
                sendDataRequest(THIS_DAY)
                binding.tvFilter.text = THIS_DAY
            }
            LAST_WEEK -> {
                sendDataRequest(LAST_WEEK)
                binding.tvFilter.text = LAST_WEEK
            }
            LAST_MONTH -> {
                sendDataRequest(LAST_MONTH)
                binding.tvFilter.text = LAST_MONTH
            }
            DATE_RANGE -> {
                datePickerFROM.show()
                binding.tvFilter.text = DATE_RANGE
            }
        }
    }
}