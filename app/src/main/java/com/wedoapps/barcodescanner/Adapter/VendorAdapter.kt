package com.wedoapps.barcodescanner.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wedoapps.barcodescanner.Model.VendorModel
import com.wedoapps.barcodescanner.R
import com.wedoapps.barcodescanner.databinding.LayoutVendorItemBinding

class VendorAdapter(
    var dataList: ArrayList<VendorModel>?,
    val listener: OnVendorClick
) :
    RecyclerView.Adapter<VendorAdapter.VendorViewHolder>() {

    inner class VendorViewHolder(private val binding: LayoutVendorItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(vendor: VendorModel) {
            binding.apply {
                tvVendorName.text = vendor.name
                tvVendorPrice.text = itemView.context.getString(R.string.Rs) + vendor.payment
                tvVendorPaidPrice.text =
                    itemView.context.getString(R.string.Rs) + vendor.paidPayment
                tvVendorDuePrice.text = itemView.context.getString(R.string.Rs) + vendor.duePayment

                btnAddPayment.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = dataList?.get(position)
                        if (item != null) {
                            listener.onAddPayment(item)
                        }
                    }
                }

                editBtn.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = dataList?.get(position)
                        if (item != null) {
                            listener.onEdit(item)
                        }
                    }
                }

                deleteBtn.setOnClickListener {
                    val position = bindingAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = dataList?.get(position)
                        if (item != null) {
                            listener.onDelete(item)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorViewHolder {
        val binding =
            LayoutVendorItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VendorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VendorViewHolder, position: Int) {
        val currentItem = dataList?.get(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    /* private val differCallback = object : DiffUtil.ItemCallback<VendorModel>() {
         override fun areItemsTheSame(oldItem: VendorModel, newItem: VendorModel): Boolean {
             return oldItem.id == newItem.id
         }

         override fun areContentsTheSame(oldItem: VendorModel, newItem: VendorModel): Boolean {
             return oldItem == newItem
         }
     }

     val differ = AsyncListDiffer(this, differCallback)*/

    interface OnVendorClick {
        fun onAddPayment(vendor: VendorModel)
        fun onEdit(vendor: VendorModel)
        fun onDelete(vendor: VendorModel)
    }

    fun filterList(filteredList: ArrayList<VendorModel>) {
        dataList = filteredList
        notifyDataSetChanged()
    }
}