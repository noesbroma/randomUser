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
import com.example.randomuser.domain.User
import com.example.randomuser.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class ListFragment : Fragment() {
    private val viewModel: ListViewModel by viewModel()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var usersList: ArrayList<User> = ArrayList()
    private var page = 1
    private var usersRecyclerAdapter: UsersRecyclerAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getUsers(page)

        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.onLoadUsersEvent.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { users ->
                progressBar.visibility = View.GONE

                linearLayoutManager = LinearLayoutManager(context)
                usersRecycler.layoutManager = linearLayoutManager
                usersRecycler.hasFixedSize()

                usersList = users

                usersRecyclerAdapter = context?.let { UsersRecyclerAdapter(requireContext(), usersList) }

                usersRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
                    {
                        super.onScrolled(recyclerView, dx, dy)
                            if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == page * 10 - 1)
                            {
                                page ++
                                viewModel.loadMore(page)
                            }
                    }
                })

                usersRecyclerAdapter?.setOnItemClickListener(object :
                    UsersRecyclerAdapter.ClickListener {
                    override fun onItemClick(v: View, position: Int) {
                        /*(activity as MainActivity).openFragment(
                            DetailFragment.newInstance(
                                usersList[position], viewModel.users
                            )
                        )*/
                    }
                })

                if (usersList.size > 0) {
                    usersRecycler.adapter?.notifyDataSetChanged()
                    usersRecycler.adapter = usersRecyclerAdapter
                } else {
                    //noResults.visibility = View.VISIBLE
                }
            }
        )

        viewModel.onLoadMoreEvent.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { users ->
                progressBar.visibility = View.GONE

                usersRecyclerAdapter?.addItems(users)
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