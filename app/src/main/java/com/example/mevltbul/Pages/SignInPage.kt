package com.example.mevltbul.Pages

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.mevltbul.R
import com.example.mevltbul.ViewModel.UserVM
import com.example.mevltbul.databinding.FragmentSignInPageBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInPage : Fragment() {


    lateinit var binding:FragmentSignInPageBinding
    lateinit var userVM:UserVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempUserVM:UserVM by viewModels()
        userVM=tempUserVM
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInPageBinding.inflate(inflater, container, false)

        binding.signInButton.setOnClickListener {
            val email=binding.loginEmail.text.toString()
            val password=binding.loginPassword.text.toString()
            if (!email.isEmpty() && !password.isEmpty()) {
                userVM.loginUser(email, password){isLogin->
                    if (isLogin){
                        Navigation.findNavController(it).navigate(R.id.action_signInPage_to_mainPage)
                    }else{
                        Toast.makeText(requireContext(), "Giriş Başarısız", Toast.LENGTH_SHORT).show()
                    }
                }

            }else{
                binding.loginEmail.error = "Email is required"
                binding.loginPassword.error = "Password is required"
            }
        }


        binding.txtRegister.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_signInPage_to_registryPage)
        }


        return binding.root
    }


}