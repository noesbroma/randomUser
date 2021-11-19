package com.example.randomuser.ui.list

//import BookmarkDataStore
import UserPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.randomuser.R
import com.example.randomuser.data.list.UsersRecyclerAdapter
import com.example.randomuser.domain.User
import com.example.randomuser.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.FieldPosition
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.randomuser.data.room.*


class ListFragment : Fragment() {
    private val viewModel: ListViewModel by viewModel()
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var usersList: ArrayList<UserRoom> = ArrayList()
    private var page = 1
    private var usersRecyclerAdapter: UsersRecyclerAdapter? = null
    //lateinit var dataStoreManager: DataStoreManager
    //private lateinit var userPreferences: UserPreferences
    //private lateinit var preferencesDataStore: BookmarkDataStore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //userPreferences = UserPreferences(requireContext())
        //preferencesDataStore = PreferencesDataStore(requireContext())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchRandomQuote()
        //viewModel.getUsers(page)

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
                            (activity as MainActivity).openFragment(
                                DetailFragment.newInstance(
                                    usersList[position]
                                )
                            )
                        }

                        override fun onTrashClick(position: Int) {
                            /*viewModel.setDeleted(usersList[position].phone)
                            usersRecyclerAdapter?.deleteItem(position)*/
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
                        (activity as MainActivity).openFragment(
                            DetailFragment.newInstance(
                                usersList[position]
                            )
                        )
                    }

                    override fun onTrashClick(position: Int) {
                        /*viewModel.setDeleted(usersList[position].phone)*/
                        var user = usersList[position]
                        var userRoom = UserRoom(user.gender, Name(user.name.first, user.name.last), user.email, user.phone, Picture(user.picture.large, user.picture.medium, user.picture.thumbnail), Location(
                        Street(user.location.street.name, user.location.street.number), user.location.city, user.location.state), Registered(user.registered.date.toString(), user.registered.age))

                        viewModel.updateUser(userRoom)

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