package com.example.randomuser.ui.list

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
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
import com.example.randomuser.ui.brouchers.SecondFragment
import kotlinx.android.synthetic.main.search_header_layout.*


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

        viewModel.getUsersFromDB()

        observeViewModel()
        setListeners()
    }


    private fun observeViewModel() {
        viewModel.onLoadUsersEvent.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { users ->
                if(users.size > 1) {
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
                            if (linearLayoutManager.findLastCompletelyVisibleItemPosition() == viewModel.usersRoom.size - 1)
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

                        override fun onTrashClick(position: Int, phone: String) {
                            var user = usersList.find { it.phone == phone }
                            user?.let { viewModel.deleteUser(it) }

                            var i = viewModel.usersRoom.indexOf(user)
                            viewModel.usersRoom.removeAt(i)

                            usersRecyclerAdapter?.deleteItem(position)

                            if (usersRecyclerAdapter?.itemCount!! > 1) {
                                noResults.visibility = View.GONE
                                usersRecycler.visibility = View.VISIBLE
                            } else {
                                noResults.visibility = View.VISIBLE
                                usersRecycler.visibility = View.GONE
                            }
                        }
                    })

                    if (usersList.size > 0) {
                        usersRecycler.adapter?.notifyDataSetChanged()
                        usersRecycler.adapter = usersRecyclerAdapter
                        usersRecycler.visibility = View.VISIBLE
                        noResults.visibility = View.GONE
                    } else {
                        noResults.visibility = View.VISIBLE
                        usersRecycler.visibility = View.GONE
                    }
                } else {
                    viewModel.getUsersFromAPI(page)
                }
            }
        )

        viewModel.onLoadMoreEvent.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { users ->
                progressBar.visibility = View.GONE

                usersRecyclerAdapter?.reloadItems(users)
            }
        )

        viewModel.onFilteredUsersEvent.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { users ->
                if (users.size > 0) {
                    usersRecycler.visibility = View.VISIBLE
                    noResults.visibility = View.GONE
                } else {
                    usersRecycler.visibility = View.GONE
                    noResults.visibility = View.VISIBLE
                }

                usersRecyclerAdapter?.reloadItems(users)
            }
        )
    }


    private fun setListeners() {
        search_edit_text.addTextChangedListener(object : TextWatcher {
            var timer: CountDownTimer? = null
            override fun afterTextChanged(text: Editable) {
                timer?.cancel()
                timer = object : CountDownTimer(1000, 1500) {
                    override fun onTick(millisUntilFinished: Long) {}
                    override fun onFinish() {
                        viewModel.filterResults(text)
                    }
                }.start()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })

        SecondActivityButton.setOnClickListener(){
            (activity as MainActivity).openFragment(
                SecondFragment.newInstance()
            )
        }
    }


    companion object {
        fun newInstance(): ListFragment {
            val fragment = ListFragment()

            return fragment
        }
    }
}