package com.example.mevltbul.Pages
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.mevltbul.R
import com.example.mevltbul.ViewModel.DetailVM
import com.example.mevltbul.databinding.FragmentAddDetailPageBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import java.util.LinkedList
import java.util.Queue

@AndroidEntryPoint
class AddDetailPage : Fragment()   {
    val imageViewList:Queue<ImageView> = LinkedList()
    lateinit var binding:FragmentAddDetailPageBinding
    private var selectedImageView:ImageView?=null
    val uriList=ArrayList<Uri?>()
    private lateinit var detailVM: DetailVM
    val bundle:AddDetailPageArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val temp :DetailVM by viewModels()
        detailVM=temp
    }



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

        binding.txtLocation.setText(bundle.address)



        binding.btnShare.setOnClickListener {

             detailVM.publishDetail(
                 System.currentTimeMillis().toString(),
                 bundle.latitude,
                 bundle.longitude,
             binding.txtDetail.text.toString(),
             uriList){
                 if(it){
                     Toast.makeText(requireContext(),"Paylaşıldı",Toast.LENGTH_LONG).show()
                     Navigation.findNavController(requireView()).navigate(R.id.action_addDetailPage_to_mainPage)

                 }else{
                     Toast.makeText(requireContext(),"Hata Paylaşılmadı",Toast.LENGTH_LONG).show()
                     Navigation.findNavController(requireView()).navigate(R.id.action_addDetailPage_to_mainPage)

                 }
             }
        }


        return binding.root
    }



@SuppressLint("SuspiciousIndentation")
fun showAllert(){
        val dialog=Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.image_upload_bottomsheet)
        val btn_camera:LinearLayout=dialog.findViewById(R.id.img_camera)
        val btn_gallery:LinearLayout=dialog.findViewById(R.id.img_gallery)

            btn_camera.setOnClickListener {
            ImagePicker.with(this).cameraOnly().start()
            dialog.dismiss()
        }
        btn_gallery.setOnClickListener {
            ImagePicker.with(this).galleryOnly().start()
            dialog.dismiss()

        }
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog.window?.setGravity(Gravity.BOTTOM)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode==Activity.RESULT_OK && requestCode ==ImagePicker.REQUEST_CODE){
            if (data != null) {
                Log.d("hatamAddDetailPageOnActiv", "data "+ data.data.toString())
                imageViewList.poll()?.setImageURI(data.data)
                uriList.add(data.data)
                if(imageViewList.size!=0){
                    imageViewList.peek()?.setImageResource(R.drawable.add_photo)
                }


            }
        }

    }




}