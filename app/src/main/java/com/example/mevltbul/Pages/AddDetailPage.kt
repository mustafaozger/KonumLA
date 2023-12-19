package com.example.mevltbul.Pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mevltbul.R
import com.example.mevltbul.databinding.FragmentAddDetailPageBinding

class AddDetailPage : Fragment() {
    lateinit var binding:FragmentAddDetailPageBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddDetailPageBinding.inflate(layoutInflater)



        return binding.root
    }


}