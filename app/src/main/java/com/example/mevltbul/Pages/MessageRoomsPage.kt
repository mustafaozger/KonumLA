package com.example.mevltbul.Pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mevltbul.databinding.FragmentEventMessagesListPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class MessageRoomsPage : Fragment() {

    lateinit var bindig:FragmentEventMessagesListPageBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindig=FragmentEventMessagesListPageBinding.inflate(inflater,container,false)



        return bindig.root
    }



}