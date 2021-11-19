package com.example.randomuser.ui.list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.randomuser.R
import com.example.randomuser.data.list.UsersRecyclerAdapter
import com.example.randomuser.data.room.UserRoom
import com.example.randomuser.domain.User
import com.example.randomuser.ui.MainActivity
import com.example.randomuser.ui.detail.DetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.user_item_row.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailFragment : Fragment() {
    private val viewModel: DetailViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { viewModel.fetchIntentData(it) }
        observeViewModel()
        setUI()
    }


    private fun setUI() {
        gender.text = viewModel.user.gender
        name.text = String.format("%s %s", viewModel.user.name.first, viewModel.user.name.last)
        location.text = String.format("%s %s, %s, %s", viewModel.user.location.street.name, viewModel.user.location.street.number, viewModel.user.location.city, viewModel.user.location.state)
        registeredDate.text = viewModel.user.registered.date.toString()
        email.text = viewModel.user .email

        val url = viewModel.user.picture.large

        if (url != "") {
            val picasso = Picasso.get()

            picasso.load(url)
                .into(photo)
        } else {
        }
    }


    private fun observeViewModel() {

    }


    companion object {
        fun newInstance(user: User): DetailFragment {
            val fragment = DetailFragment()

            val args = Bundle()
            args.putSerializable("EXTRA_USER", user)
            fragment.arguments = args

            return fragment
        }
    }
}