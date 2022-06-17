package com.wedoapps.barcodescanner.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.wedoapps.barcodescanner.Model.SingleReportModel
import com.wedoapps.barcodescanner.R
import com.wedoapps.barcodescanner.databinding.LayoutSingleReportItemBinding

class ReportSingleItemAdapter :
    RecyclerView.Adapter<ReportSingleItemAdapter.ReportSingleItemViewHolder>() {

    inner class ReportSingleItemViewHolder(private val binding: LayoutSingleReportItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(singleReportModel: SingleReportModel) {
            binding.apply {
                tvSingleItemCount.text = ("${bindingAdapterPosition + 1}.").toString()
                tvSingleItemName.text = singleReportModel.itemName
                tvSingleItemBarcode.text = singleReportModel.barcodeNumber
                tvSingleItemSinglePrice.text =
                    "${itemView.context.getString(R.string.Rs)}${singleReportModel.itemPrice.toString()}"
                tvSingleItemQuantity.text = "${singleReportModel.quantity} x"

                tvSingleItemTotalPrice.text =
                    "${itemView.context.getString(R.string.Rs)}${
                        (singleReportModel.itemPrice?.times(
                            singleReportModel.quantity!!
                        ))
                    }"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportSingleItemViewHolder {
        val binding = LayoutSingleReportItemBinding.inflate(
            LayoutInflater.from(
                parent.context
            ),
            parent,
            false
        )
        return ReportSingleItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportSingleItemViewHolder, position: Int) {
        val currenItem = differ.currentList[position]

        if (currenItem != null) {
            holder.bind(currenItem)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private val differCallback = object : DiffUtil.ItemCallback<SingleReportModel>() {
        override fun areItemsTheSame(
            oldItem: SingleReportModel,
            newItem: SingleReportModel
        ): Boolean {
            return true
        }

        override fun areContentsTheSame(
            oldItem: SingleReportModel,
            newItem: SingleReportModel
        ): Boolean {
            return true
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}