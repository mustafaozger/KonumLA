package com.example.mevltbul.Pages

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.R
import com.example.mevltbul.Repository.DetailPageDaoRepository
import com.example.mevltbul.databinding.FragmentAddDetailPageBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.constant.ImageProvider
import java.util.LinkedList
import java.util.Queue
import java.util.Stack

class AddDetailPage : Fragment()  {
    val imageViewList:Queue<ImageView> = LinkedList()
    val uriList: Queue<Uri> = LinkedList()
    lateinit var binding:FragmentAddDetailPageBinding
    private var selectedImageView:ImageView?=null
    val hashMap=HashMap<ImageView,Uri?>()

    init {
        uriList.add(null)
        uriList.add(null)
        uriList.add(null)
        uriList.add(null)

    }


//    val launcher=registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()){
//        if (it.resultCode==Activity.RESULT_OK){
//            if (it.data != null ) {
//                imageViewList.poll()?.setImageURI(it.data!!.data)
//                uriList.poll()
//                uriList.add(it.data!!.data)
//                if(imageViewList.size!=0){
//                    imageViewList.peek()?.setImageResource(R.drawable.background_fotograf_ekle3)
//                }
////                selectedImageView?.setImageURI(data.data)
////                data.data?.let { selectedImageView?.let { it1 -> hashMap.put(it1, it) } }
////
//        }
//    }
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAddDetailPageBinding.inflate(layoutInflater)
        imageViewList.add(binding.addImage1)
        imageViewList.add(binding.addImage2)
        imageViewList.add(binding.addImage3)
        imageViewList.add(binding.addImage4)

        binding.addImage1.setOnClickListener {
            selectedImageView=binding.addImage1
            showAllert()


        }
        binding.textView2.setOnClickListener{
            selectedImageView=binding.addImage2
            showAllert()
        }
        binding.addImage3.setOnClickListener {
            selectedImageView=binding.addImage3
            showAllert()
        }
        binding.addImage4.setOnClickListener {
            selectedImageView=binding.addImage4
            showAllert()
        }




        binding.btnShare.setOnClickListener {
            val mar=Marker(
                "dsa",
                "dsdas",
                "dsad",
                binding.txtDetail.text.toString(),
                uriList.poll()?.toString(),
                uriList.poll()?.toString(),
                uriList.poll()?.toString(),
                uriList.poll()?.toString()
            )

            val pr=DetailPageDaoRepository()
             val result=pr.uploadImage(requireContext(),mar)



        }


        return binding.root
    }




//    private fun showAllert(){
//
//        val dialog=Dialog(requireContext())
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setCancelable(false)
//        dialog.setContentView(R.layout.dialog_box_choose_camera_or_media)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//
//        val btnCamera: ImageView =dialog.findViewById(R.id.img_allert_camera)
//        val btnGallery: ImageView =dialog.findViewById(R.id.img_allert_gallery)
//
//        btnCamera.setOnClickListener {
//            ImagePicker.with(this).cameraOnly().start()
//        }
//        btnGallery.setOnClickListener {
//            ImagePicker.with(this).galleryOnly()
//        }
//
//        dialog.show()
//
//    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==Activity.RESULT_OK && requestCode ==ImagePicker.REQUEST_CODE){
            if (data != null) {
                imageViewList.poll()?.setImageURI(data.data)
                uriList.poll()
                uriList.add(data.data)
                if(imageViewList.size!=0){
                    imageViewList.peek()?.setImageResource(R.drawable.background_fotograf_ekle3)
                }
//                selectedImageView?.setImageURI(data.data)
//                data.data?.let { selectedImageView?.let { it1 -> hashMap.put(it1, it) } }
//

            }
        }

    }




    private fun  showAllert(){
        val builder=AlertDialog.Builder(requireContext())
        val customView=LayoutInflater.from(requireContext()).inflate(R.layout.dialog_box_choose_camera_or_media,null)
        builder.setView(customView)


        val dialog=builder.create()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)


        val camera=customView.findViewById<ImageView>(R.id.img_allert_camera)
        val gallery=customView.findViewById<ImageView>(R.id.img_allert_gallery)

        camera.setOnClickListener {
            ImagePicker.with(requireActivity()).cameraOnly().start()
            dialog.dismiss()
        }
        gallery.setOnClickListener {
            ImagePicker.with(requireActivity()).galleryOnly().start()
            dialog.dismiss()
        }
        dialog.show()

    }


}