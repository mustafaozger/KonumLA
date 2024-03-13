package com.example.mevltbul.Pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.mevltbul.R
import com.example.mevltbul.ViewModel.UserVM
import com.example.mevltbul.databinding.FragmentRegistryPageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistryPage : Fragment() {

    lateinit var binding: FragmentRegistryPageBinding
    lateinit var userVM: UserVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempUserVM:UserVM by viewModels()
        userVM = tempUserVM
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentRegistryPageBinding.inflate(inflater,container,false)
        
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener{
            val username = binding.txtRegisterUserName.text.toString()
            val password = binding.txtRegisterPassword.text.toString()
            val confirmPassword = binding.txtRegisterPasswordConfirm.text.toString()
            val email=binding.txtRegisterEmail.text.toString()
            if (password==confirmPassword){
                if(password!=""&&username!=""&&email!=""){
                    userVM.createUser(username,email,password){
                        Toast.makeText(context,"Kullanıcı olusturuldu",Toast.LENGTH_SHORT).show()
                        Navigation.findNavController(requireView()).navigate(R.id.action_registryPage_to_mainPage)
                    }
                }else{
                    Toast.makeText(context,"Lütfen bilgileri eksiksiz doldurun",Toast.LENGTH_SHORT).show()
                }


            }else{
                Toast.makeText(context,"Sifreler uyusmuyor",Toast.LENGTH_SHORT).show()

            }

            binding.txtGotoLoginPage.setOnClickListener {
                Navigation.findNavController(it).navigate(R.id.action_registryPage_to_signInPage2)
            }


        }

    }

}