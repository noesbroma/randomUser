package com.example.randomuser.ui.brouchers

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.randomuser.R
import com.example.randomuser.data.brochures.BroucherRecyclerAdapter
import com.example.randomuser.data.list.UsersRecyclerAdapter
import com.example.randomuser.data.room.*
import com.example.randomuser.domain.Broucher
import com.example.randomuser.domain.Preview
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_second.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SecondFragment : Fragment() {
    private val viewModel: BrouchersViewModel by viewModel()
    private lateinit var gridLayoutManager: GridLayoutManager
    private var brouchersRecyclerAdapter: BroucherRecyclerAdapter? = null
    private var offset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getBrouchersFromAPI()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.onLoadBrouchersEvent.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { items ->
                if (items.size > 1) {
                    gridLayoutManager = GridLayoutManager(context, 2)
                    brouchersRecycler.layoutManager = gridLayoutManager
                    brouchersRecycler.hasFixedSize()

                    brouchersRecyclerAdapter = context?.let { BroucherRecyclerAdapter(items) }

                    brouchersRecycler.adapter = brouchersRecyclerAdapter

                    brouchersRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int)
                        {
                            super.onScrolled(recyclerView, dx, dy)
                            if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == viewModel.catalogsList.size - 1)
                            {
                                offset ++
                                viewModel.loadMore(offset)
                            }
                        }
                    })
                } else {
                }
            }
        )

        viewModel.onLoadMoreEvent.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { users ->
                brouchersRecyclerAdapter?.reloadItems(users)
            }
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SecondFragment().apply {
            }
    }
}