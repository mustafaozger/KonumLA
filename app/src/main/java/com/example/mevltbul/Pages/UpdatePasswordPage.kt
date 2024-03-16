package com.example.mevltbul.Pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.mevltbul.R
import com.example.mevltbul.ViewModel.UserVM
import com.example.mevltbul.databinding.FragmentUpdatePasswordBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
@AndroidEntryPoint
class UpdatePasswordPage : Fragment() {

    private lateinit var userVM: UserVM
    private lateinit var binding: FragmentUpdatePasswordBinding

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
        bottomNavigationView.visibility=View.GONE
        val fabButton=requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fabButton.visibility=View.GONE

        binding= FragmentUpdatePasswordBinding.inflate(inflater,container,false)

        binding.btnSavePassword.setOnClickListener{
            val currentPass:String= binding.txtCurrentPassword.text.toString()
            val password:String= binding.txtNewPassword.text.toString()
            val confirmPassword:String= binding.txtNewPasswordConfirm.text.toString()
            if(password==confirmPassword){
               userVM.checkUserPassword(currentPass){
                   if(it){
                       userVM.changePassword(password){
                           if (it){
                               Toast.makeText(requireContext(),"Şifreniz değiştirildi",Toast.LENGTH_SHORT).show()
                               Navigation.findNavController(requireView()).navigate(R.id.action_updatePasswordPage_to_accountPage)
                           }else{
                               Toast.makeText(requireContext(),"Şifreniz değiştirilemedi",Toast.LENGTH_SHORT).show()
                           }
                       }
                   }else{
                       Toast.makeText(requireContext(),"Hatalı şifre",Toast.LENGTH_SHORT).show()
                   }
               }
            }else{
                Toast.makeText(requireContext(),"Şifreler uyuşmuyor",Toast.LENGTH_SHORT).show()
            }
        }




        return binding.root
    }


}