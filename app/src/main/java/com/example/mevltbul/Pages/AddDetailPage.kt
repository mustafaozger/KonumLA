package com.example.mevltbul.Pages

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.R
import com.example.mevltbul.Repository.DetailPageDaoRepository
import com.example.mevltbul.databinding.FragmentAddDetailPageBinding
import com.github.dhaval2404.imagepicker.ImagePicker

class AddDetailPage : Fragment() {
    lateinit var binding:FragmentAddDetailPageBinding
    var imagePicker:ImageView?=null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddDetailPageBinding.inflate(layoutInflater)

        binding.addImage1.setOnClickListener {
            showAllert()
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

    private fun takePhoto(){

    }


    private fun showAllert(){

        val dialog=Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_box_choose_camera_or_media)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        val btnCamera: ImageView =dialog.findViewById(R.id.img_allert_camera)
        val btnGallery: ImageView =dialog.findViewById(R.id.img_allert_gallery)

        btnCamera.setOnClickListener {
            ImagePicker.with(this).cameraOnly().start()
        }
        btnGallery.setOnClickListener {
            ImagePicker.with(this).galleryOnly()
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK && requestCode ==ImagePicker.REQUEST_CODE){

            binding.addImage2.setImageURI(data?.data)
        }

    }

}