package com.example.mevltbul.Pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mevltbul.R
import com.example.mevltbul.ViewModel.UserVM
import com.example.mevltbul.databinding.FragmentAccountPageBinding
import kotlinx.coroutines.launch


class AccountPage : Fragment() {

    private lateinit var binding: FragmentAccountPageBinding
    private lateinit var userVM: UserVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempUserVM:UserVM by viewModels()
        userVM=tempUserVM
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAccountPageBinding.inflate(inflater, container, false)
        userVM.getUserData()
        lifecycleScope.launch{
            userVM.userData.collect{
            }
        }

        return binding.root
    }

}