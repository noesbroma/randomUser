package com.example.randomuser.data.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.randomuser.R
import com.example.randomuser.domain.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_item_row.view.*
import java.util.*


class UsersRecyclerAdapter(
        private val context: Context,
        var users: ArrayList<User>
): RecyclerView.Adapter<UsersRecyclerAdapter.UserHolder>(){


    private var clickListener: ClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item_row, parent, false)

        return UserHolder(v, 0)
    }


    override fun getItemCount(): Int {
        return users.size
    }


    fun getItemName(position: Int): String? {
        return String.format("%s %s", users?.get(position).name.first, users?.get(position).name.last)
    }


    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }


    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val data = users?.get(position)
        data?.let { holder.bindItems(it, position) }
    }


    inner class UserHolder(itemView: View, position: Int) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        init {
            if (clickListener != null) {
                itemView.setOnClickListener(this)
            }
        }


        fun bindItems(user: User, position: Int) {
            itemView.name.text = getItemName(position)
            itemView.email.text = user.email
            itemView.phone.text = user.phone

            val url = user.picture.large

            if (url != "") {
                val picasso = Picasso.get()

                picasso.load(url)
                    .into(itemView.photo)
            } else {
            }
        }


        override fun onClick(v: View?) {
            if (v != null) {
                clickListener?.onItemClick(v,adapterPosition)
            }
        }
    }

    interface ClickListener {
        fun onItemClick(v: View, position: Int)
    }
}