package com.example.randomuser.data.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.randomuser.R
import com.example.randomuser.data.room.UserRoom
import com.example.randomuser.domain.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.user_item_row.view.*
import java.util.*
import kotlin.collections.ArrayList


class UsersRecyclerAdapter(var users: ArrayList<UserRoom>
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


    fun addItems(users: ArrayList<UserRoom>) {
        this.users.addAll(users)
        notifyDataSetChanged()
    }


    fun reloadItems(users: ArrayList<UserRoom>) {
        this.users = users
        notifyDataSetChanged()
    }


    fun deleteItem(position: Int) {
        this.users.removeAt(position)
        notifyDataSetChanged()
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


        fun bindItems(user: UserRoom, position: Int) {
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

            itemView.trash.setOnClickListener(View.OnClickListener {
                clickListener?.onTrashClick(position, itemView.phone.text.toString())
            })
        }


        override fun onClick(v: View?) {
            if (v != null) {
                clickListener?.onItemClick(v, adapterPosition)
            }
        }
    }

    interface ClickListener {
        fun onItemClick(v: View, position: Int)
        fun onTrashClick(position: Int, phone: String)
    }
}