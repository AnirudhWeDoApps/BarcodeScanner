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
import com.wedoapps.barcodescanner.Utils.Constants
import com.wedoapps.barcodescanner.databinding.ActivityChartBuyerReportBinding

class ChartBuyerReportActivity : AppCompatActivity(), OnChartValueSelectedListener {

    private lateinit var binding: ActivityChartBuyerReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBuyerReportBinding.inflate(layoutInflater)
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

        setData(5, 45f)



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
            chartPie.isRotationEnabled = true
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


    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        Toast.makeText(this, e?.data.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onNothingSelected() {
        Log.d(Constants.TAG, "onNothingSelected: ")
    }

    private fun setData(count: Int, range: Float) {
        val entries: ArrayList<PieEntry> = ArrayList()
        val parties = arrayListOf(
            1, 2, 4, 5, 6, 3, 2, 3, 45, 66, 5, 434, 56, 54, 34, 54, 34, 5
        )

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until count) {
            entries.add(
                PieEntry(
                    (Math.random() * range + range / 5).toFloat(),
                    parties[i % parties.size],
                )
            )
        }
        val dataSet = PieDataSet(entries, "Election Results")
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
}