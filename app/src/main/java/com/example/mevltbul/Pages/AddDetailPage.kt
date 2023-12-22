package com.example.mevltbul.Pages
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mevltbul.Classes.Marker
import com.example.mevltbul.R
import com.example.mevltbul.Repository.DetailPageDaoRepository
import com.example.mevltbul.databinding.FragmentAddDetailPageBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.github.dhaval2404.imagepicker.ImagePickerActivity
import com.github.dhaval2404.imagepicker.ImagePickerFileProvider
import java.util.LinkedList
import java.util.Queue

class AddDetailPage : Fragment()  {
    val imageViewList:Queue<ImageView> = LinkedList()
    lateinit var binding:FragmentAddDetailPageBinding
    private var selectedImageView:ImageView?=null
    val uriList=ArrayList<Uri?>()



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
        binding.addImage2.setOnClickListener{
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


            val pr=DetailPageDaoRepository()
             val result=pr.publishDetail(requireContext(),"mar","321231","2313","dskadasas",uriList)



        }


        return binding.root
    }


    fun showAllert(){
        val builder=AlertDialog.Builder(requireContext())
        val allertConext=LayoutInflater.from(requireContext()).inflate(R.layout.dialog_box_choose_camera_or_media,null)
        builder.setView(allertConext)
        val dialog=builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val btn_camera: ImageView? =allertConext.findViewById(R.id.img_allert_camera)
        val btn_gallery:ImageView?=allertConext.findViewById(R.id.img_allert_gallery)

        btn_camera?.setOnClickListener {
            ImagePicker.with(this).cameraOnly().start()
            dialog.dismiss()
        }
        btn_gallery?.setOnClickListener {
            ImagePicker.with(this).galleryOnly().start()
            dialog.dismiss()

        }
        dialog.show()

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("hatamAddDetailPageOnActiv", "work")

        if(resultCode==Activity.RESULT_OK && requestCode ==ImagePicker.REQUEST_CODE){
            if (data != null) {
                Log.d("hatamAddDetailPageOnActiv", "data "+ data.data.toString())
                imageViewList.poll()?.setImageURI(data.data)
                uriList.add(data.data)
                if(imageViewList.size!=0){
                    imageViewList.peek()?.setImageResource(R.drawable.background_fotograf_ekle3)
                }


            }
        }

    }




}