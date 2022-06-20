package com.wedoapps.barcodescanner.Ui.Report.BuyerReport

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.wedoapps.barcodescanner.Adapter.DataRecyclerAdapter
import com.wedoapps.barcodescanner.Model.BuyerReportModal
import com.wedoapps.barcodescanner.Model.ScannedData
import com.wedoapps.barcodescanner.Utils.Constants
import com.wedoapps.barcodescanner.Utils.Constants.BUYER_DATA
import com.wedoapps.barcodescanner.databinding.ActivityChartBuyerReportBinding

class ChartBuyerReportActivity : AppCompatActivity(), OnChartValueSelectedListener {

    private lateinit var binding: ActivityChartBuyerReportBinding
    private lateinit var buyerData: BuyerReportModal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBuyerReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buyerData = intent.extras?.getParcelable(BUYER_DATA)!!

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

        binding.apply {
            chartPie.setUsePercentValues(true)
            chartPie.description.isEnabled = false
            chartPie.setExtraOffsets(5f, 10f, 5f, 5f)

            chartPie.dragDecelerationFrictionCoef = 0.95f

//            chartPie.centerText = generateCenterSpannableText()

            chartPie.isDrawHoleEnabled = true
            chartPie.setHoleColor(Color.WHITE)

            chartPie.setTransparentCircleColor(Color.WHITE)
            chartPie.setTransparentCircleAlpha(110)

            chartPie.holeRadius = 58f
            chartPie.transparentCircleRadius = 61f

            chartPie.setDrawCenterText(true)

            chartPie.rotationAngle = 0f
            // enable rotation of the chartPie by touch
            // enable rotation of the chartPie by touch
            chartPie.isRotationEnabled = false
            chartPie.isHighlightPerTapEnabled = true

            // chartPie.setUnit(" €");
            // chartPie.setDrawUnitsInchartPie(true);

            // add a selection listener

            // chartPie.setUnit(" €");
            // chartPie.setDrawUnitsInchartPie(true);

            // add a selection listener
            chartPie.setOnChartValueSelectedListener(this@ChartBuyerReportActivity)

            chartPie.animateY(1400, Easing.EaseInOutQuad)
            // chartPie.spin(2000, 0, 360);

            // chartPie.spin(2000, 0, 360);
            val l: Legend = chartPie.legend
            l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
            l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            l.orientation = Legend.LegendOrientation.VERTICAL
            l.setDrawInside(false)
            l.xEntrySpace = 7f
            l.yEntrySpace = 0f
            l.yOffset = 0f

            // entry label styling

            // entry label styling
            chartPie.setEntryLabelColor(Color.WHITE)
            chartPie.setEntryLabelTextSize(12f)
        }

        if (buyerData.cartList.isNullOrEmpty()) {
            showEmpty()
        } else {
            hideEmpty()
            val list = buyerData.cartList
            val filterdList = list?.groupBy { s -> s.barcodeNumber }?.values?.map {
                ScannedData(
                    it[0].id,
                    it[0].barcodeNumber,
                    it[0].itemCode,
                    it[0].item,
                    it.sumOf { i -> i.price!! },
                    it[0].originalPrice,
                    it[0].storeQuantity,
                    it[0].minCount,
                    it[0].showDialog,
                    it.sumOf { i -> i.count!! }
                )
            }
            val cartAdapter = DataRecyclerAdapter(filterdList)
            binding.rvBuyerList.apply {
                setHasFixedSize(true)
                adapter = cartAdapter
            }

            setData(filterdList)

        }

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Toast.makeText(this, e?.data.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected() {
        Log.d(Constants.TAG, "onNothingSelected: ")
    }

    private fun setData(filterdList: List<ScannedData>?) {
        val entries: ArrayList<PieEntry> = ArrayList()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in filterdList?.indices!!) {
            entries.add(
                PieEntry(
                    filterdList[i].count?.toFloat()!!,
                    filterdList[i].item
                )
            )
        }
        val dataSet = PieDataSet(entries, "${buyerData.name}'s Item List")
        dataSet.setDrawIcons(false)
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors
        val colors: ArrayList<Int> = ArrayList()
        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)
        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)
        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)
        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)
        colors.add(ColorTemplate.getHoloBlue())
        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)
        data.setValueTextColor(Color.WHITE)
        binding.chartPie.data = data

        // undo all highlights
        binding.chartPie.highlightValues(null)
        binding.chartPie.invalidate()
    }

    private fun hideEmpty() {
        binding.apply {
            ivNoData.visibility = View.GONE
            rvBuyerList.visibility = View.VISIBLE
            chartPie.visibility = View.VISIBLE
        }
    }

    private fun showEmpty() {
        binding.apply {
            ivNoData.visibility = View.VISIBLE
            rvBuyerList.visibility = View.GONE
            chartPie.visibility = View.GONE
        }
    }

}