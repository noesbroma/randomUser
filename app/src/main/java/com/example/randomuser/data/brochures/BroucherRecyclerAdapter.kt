package com.example.randomuser.data.brochures

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.randomuser.R
import com.example.randomuser.data.room.UserRoom
import com.example.randomuser.domain.Broucher
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.broucher_item_row.view.*
import kotlin.collections.ArrayList


class BroucherRecyclerAdapter(var catalog: ArrayList<Broucher>
): RecyclerView.Adapter<BroucherRecyclerAdapter.BroucherHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BroucherHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.broucher_item_row, parent, false)

        return BroucherHolder(v, 0)
    }


    override fun getItemCount(): Int {
        return catalog.size
    }


    fun reloadItems(brouchers: ArrayList<Broucher>) {
        this.catalog = brouchers
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: BroucherHolder, position: Int) {
        val data = catalog?.get(position)
        data?.let { holder.bindItems(it, position) }
    }


    inner class BroucherHolder(itemView: View, position: Int) : RecyclerView.ViewHolder(itemView) {
        fun bindItems(broucher: Broucher, position: Int) {
            itemView.title.text = broucher.title

            val url = broucher.previewUrls.w375

            if (url != "") {
                val picasso = Picasso.get()

                picasso.load(url)
                    .into(itemView.photo)
            } else {
            }
        }
    }
}