package com.example.mevltbul.Pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.R
import com.example.mevltbul.Repository.DetailPageDaoRepository
import com.example.mevltbul.databinding.FragmentAddDetailPageBinding

class AddDetailPage : Fragment() {
    lateinit var binding:FragmentAddDetailPageBinding
    var urip=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddDetailPageBinding.inflate(layoutInflater)

        binding.addImage1.setOnClickListener {

        }



        binding.btnShare.setOnClickListener {
            val mar=Marker(
                "ewd",
                "dsdas",
                "dsad",
                binding.txtDetail.text.toString(),
                "sadasd",
                "dsad",
            )

            val pr=DetailPageDaoRepository()
             val result=pr.uploadImage(requireContext(),mar)



        }


        return binding.root
    }

}