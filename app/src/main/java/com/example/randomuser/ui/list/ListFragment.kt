package com.example.randomuser.ui.list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.randomuser.R
import com.example.randomuser.data.list.UsersRecyclerAdapter
import com.example.randomuser.domain.User
import com.example.randomuser.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListFragment : Fragment() {
    private val viewModel: ListViewModel by viewModel()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var usersList: ArrayList<User> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUsers()

        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.onLoadUsersEvent.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { users ->
                linearLayoutManager = LinearLayoutManager(context)
                usersRecycler.layoutManager = linearLayoutManager
                usersRecycler.hasFixedSize()

                usersList = users

                val mAdapter = context?.let { UsersRecyclerAdapter(requireContext(), usersList) }

                /*mAdapter?.setOnItemClickListener(object :
                    UsersRecyclerAdapter.ClickListener {
                    override fun onItemClick(v: View, position: Int) {
                        (activity as MainActivity).openFragment(
                            DetailFragment.newInstance(
                                usersList[position], viewModel.users
                            )
                        )
                    }
                })*/

                if (usersList.size > 0) {
                    usersRecycler.adapter?.notifyDataSetChanged()
                    usersRecycler.adapter = mAdapter
                } else {
                    //noResults.visibility = View.VISIBLE
                }
            }
        )
    }


    companion object {
        fun newInstance(): ListFragment {
            val fragment = ListFragment()

            return fragment
        }
    }
}