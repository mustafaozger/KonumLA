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
import com.example.mevltbul.databinding.FragmentUpdateUserNamePageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateUserNamePage : Fragment() {

    private lateinit var binding:FragmentUpdateUserNamePageBinding
    private lateinit var userVM: UserVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val temp:UserVM by viewModels()
        userVM=temp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val bottomNavigationView=requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility=View.GONE
        val fabButton=requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fabButton.visibility=View.GONE


        binding=FragmentUpdateUserNamePageBinding.inflate(layoutInflater)

        binding.btnSaveUserName.setOnClickListener {
            val userName=binding.txtNewUserName.text.toString()
            val password=binding.txtNewPassword.text.toString()
            if (!userName.isEmpty()){
                userVM.checkUserPassword(password){
                    if(it){
                        userVM.changeUserName(userName){
                            if (it){
                                Toast.makeText(requireContext(),"Kullanıcı Adı Değiştirildi",Toast.LENGTH_LONG).show()
                                Navigation.findNavController(requireView()).navigate(R.id.action_updateUserNamePage_to_accountPage)
                            }else{
                                Toast.makeText(requireContext(),"Hata: Kullanıcı Adı Değiştirilemdi",Toast.LENGTH_LONG).show()
                            }
                        }

                    }else{
                        Toast.makeText(requireContext(),"Parola Hatalı",Toast.LENGTH_LONG).show()
                    }
                }
            }else{
                Toast.makeText(requireContext(),"Kullanıcı adı boş",Toast.LENGTH_LONG).show()
            }
        }


        binding.btnBackUserToAccountPage.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_updateUserNamePage_to_accountPage)
        }


        return binding.root
    }

}