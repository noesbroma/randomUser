package com.example.randomuser.ui.list

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.randomuser.R
import com.example.randomuser.data.list.UsersRecyclerAdapter
import com.example.randomuser.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.randomuser.data.room.UserRoom
import com.example.randomuser.domain.*


class ListFragment : Fragment() {
    private val viewModel: ListViewModel by viewModel()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var usersList: ArrayList<UserRoom> = ArrayList()
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

        viewModel.fetchRandomQuote()

        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.liveQuote.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { usersRoom ->
                if(usersRoom.size > 1) {
                    progressBar.visibility = View.GONE

                    linearLayoutManager = LinearLayoutManager(context)
                    usersRecycler.layoutManager = linearLayoutManager
                    usersRecycler.hasFixedSize()
                    usersRecycler.addItemDecoration(
                        DividerItemDecoration(
                            context,
                            DividerItemDecoration.VERTICAL
                        )
                    )

                    usersList = usersRoom as ArrayList<UserRoom>

                    usersRecyclerAdapter = context?.let { UsersRecyclerAdapter(usersList) }

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
                            val userRoom = usersList[position]
                            var user = User(userRoom.gender, Name(userRoom.name.title, userRoom.name.first, userRoom.name.last), userRoom.email, userRoom.phone, Picture(userRoom.picture.large, userRoom.picture.medium, userRoom.picture.thumbnail), Location(
                                Street(userRoom.location.street.name, userRoom.location.street.number), userRoom.location.city, userRoom.location.state), Registered(userRoom.registered.date, userRoom.registered.age)
                            )

                            (activity as MainActivity).openFragment(
                                DetailFragment.newInstance(
                                    user
                                )
                            )
                        }

                        override fun onTrashClick(position: Int) {
                            var user = usersList[position]

                            viewModel.updateUser(user)

                            usersRecyclerAdapter?.deleteItem(position)
                        }
                    })

                    if (usersList.size > 0) {
                        usersRecycler.adapter?.notifyDataSetChanged()
                        usersRecycler.adapter = usersRecyclerAdapter
                    } else {
                        //noResults.visibility = View.VISIBLE
                    }
                } else {
                    viewModel.getUsers(page)
                }
            }
        )

        viewModel.onLoadUsersEvent.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { users ->
                progressBar.visibility = View.GONE

                linearLayoutManager = LinearLayoutManager(context)
                usersRecycler.layoutManager = linearLayoutManager
                usersRecycler.hasFixedSize()
                usersRecycler.addItemDecoration(
                    DividerItemDecoration(
                        context,
                        DividerItemDecoration.VERTICAL
                    )
                )

                usersList = users

                usersRecyclerAdapter = context?.let { UsersRecyclerAdapter(usersList) }

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
                        val userRoom = usersList[position]
                        var user = User(userRoom.gender, Name(userRoom.name.title, userRoom.name.first, userRoom.name.last), userRoom.email, userRoom.phone, Picture(userRoom.picture.large, userRoom.picture.medium, userRoom.picture.thumbnail), Location(
                            Street(userRoom.location.street.name, userRoom.location.street.number), userRoom.location.city, userRoom.location.state), Registered(userRoom.registered.date, userRoom.registered.age)
                        )

                        (activity as MainActivity).openFragment(
                            DetailFragment.newInstance(
                                user
                            )
                        )
                    }

                    override fun onTrashClick(position: Int) {
                        var user = usersList[position]

                        viewModel.updateUser(user)

                        usersRecyclerAdapter?.deleteItem(position)
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