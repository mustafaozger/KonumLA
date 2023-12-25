package com.example.mevltbul.Pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mevltbul.databinding.FragmentMainPageBinding

class MainPage: Fragment() {
        lateinit var binding: FragmentMainPageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentMainPageBinding.inflate(layoutInflater)
        return binding.root
    }

}