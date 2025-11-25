package com.example.parkmobile.ui.estacionamento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.parkmobile.R
import com.example.parkmobile.data.repository.CheckOutRepository

class CheckOutFragment : Fragment() {

    private lateinit var checkOutRepository: CheckOutRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_out, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvCheckOut = view.findViewById<RecyclerView>(R.id.rv_check_out)

        // Initialize repository and get data
        checkOutRepository = CheckOutRepository()
        val checkOutList = checkOutRepository.getCheckOutItems()

        // Pass the childFragmentManager to the adapter
        rvCheckOut.adapter = CheckOutAdapter(checkOutList, childFragmentManager)
    }
}
