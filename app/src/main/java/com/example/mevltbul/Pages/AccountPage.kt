package com.example.mevltbul.Pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.mevltbul.R
import com.example.mevltbul.ViewModel.UserVM
import com.example.mevltbul.databinding.FragmentAccountPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
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

        val bottomNavigationView=requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility=View.VISIBLE
        val fabButton=requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fabButton.visibility=View.VISIBLE


        binding = FragmentAccountPageBinding.inflate(inflater, container, false)
        userVM.getUserData()
        lifecycleScope.launch{
            userVM.userData.collect{
                binding.txtAccountUserName.text=it.user_name
                binding.txtAccountUserEmail.text=it.user_mail
            }
        }

        binding.btnChangePassword.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_accountPage_to_updatePasswordPage)
        }
        binding.btnChangeUserName.setOnClickListener {

        }

        return binding.root
    }

}