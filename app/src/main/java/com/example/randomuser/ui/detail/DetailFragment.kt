package com.example.randomuser.ui.list

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.randomuser.R
import com.example.randomuser.domain.User
import com.example.randomuser.ui.detail.DetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


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
        val zonedTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZonedDateTime.parse(viewModel.user.registered.date)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val formattedDate : String = zonedTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        gender.text = viewModel.user.gender.capitalize()
        name.text = String.format("%s %s", viewModel.user.name.first, viewModel.user.name.last)
        location.text = String.format("%s %s, %s, %s", viewModel.user.location.street.name, viewModel.user.location.street.number, viewModel.user.location.city, viewModel.user.location.state)
        registeredDate.text = formattedDate
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