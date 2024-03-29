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
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.mevltbul.R
import com.example.mevltbul.ViewModel.DetailVM
import com.example.mevltbul.databinding.FragmentAddDetailPageBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomNavigationView=requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility=View.GONE
        val fabButton=requireActivity().findViewById<FloatingActionButton>(R.id.floatingActionButton)
        fabButton.visibility=View.GONE
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val temp :DetailVM by viewModels()
        detailVM=temp
    }



    @SuppressLint("SimpleDateFormat")
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
            publish()
        }

        binding.btnDetailPageBack.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val eventNameList=resources.getStringArray(R.array.event_name_list)
        val arrayAdapter=ArrayAdapter(requireContext(),R.layout.event_list_dropdown_item,eventNameList)
        binding.txtEventList.setAdapter(arrayAdapter)

    }

@SuppressLint("SuspiciousIndentation")
fun showAllert(){
        val dialog=Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.image_upload_bottomsheet)
        val btn_camera:LinearLayout=dialog.findViewById(R.id.img_camera)
        val btn_gallery:LinearLayout=dialog.findViewById(R.id.img_gallery)

            btn_camera.setOnClickListener {
                // max size of image 1 MB
                showProgressBar()

                ImagePicker.with(this).cameraOnly().compress(1024)
                .createIntent { intent ->
                startForProfileImageResult.launch(intent)
            }

            dialog.dismiss()
        }
        btn_gallery.setOnClickListener {
            // max size of image 1 MB
            showProgressBar()
            ImagePicker.with(this).galleryOnly().compress(1024).createIntent { intent ->
                startForProfileImageResult.launch(intent)

            }
            dialog.dismiss()

        }
        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.setWindowAnimations(R.style.DialogAnimation)
        dialog.window?.setGravity(Gravity.BOTTOM)
    }

    private val startForProfileImageResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->

        val resultCode = result.resultCode
        val data = result.data
        if (resultCode == Activity.RESULT_OK) {

            if (data != null) {
                imageViewList.poll()?.setImageURI(data.data)
                uriList.add(data.data)
                if(imageViewList.size!=0){
                    imageViewList.peek()?.setImageResource(R.drawable.add_photo)
                }
                hideProgressBar()
            }
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            hideProgressBar()
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            hideProgressBar()
            Toast.makeText(requireContext(), "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }




    private fun publish(){

        if (binding.txtEventList.text.toString()!="" ){
            showProgressBar()
            val sdf=SimpleDateFormat("dd/MM/yyyy HH:mm")
            val currentDAte=sdf.format(System.currentTimeMillis())
            detailVM.publishDetail(
                System.currentTimeMillis().toString(),
                binding.txtEventName.text.toString(),
                bundle.latitude,
                bundle.longitude,
                binding.txtDetail.text.toString(),
                uriList,
                binding.txtEventList.text.toString(),currentDAte.toString()){
                if(it){
                    hideProgressBar()
                    Toast.makeText(requireContext(),"Paylaşıldı",Toast.LENGTH_LONG).show()
                    Navigation.findNavController(requireView()).navigate(R.id.action_addDetailPage_to_mainPage)

                }else{
                    hideProgressBar()
                    Toast.makeText(requireContext(),"Hata Paylaşılmadı",Toast.LENGTH_LONG).show()
                    Navigation.findNavController(requireView()).navigate(R.id.action_addDetailPage_to_mainPage)

                }
            }
        }else{
            Toast.makeText(requireContext(),"Etkinlik Türünü Seçin",Toast.LENGTH_LONG).show()
        }


    }



    @SuppressLint("ClickableViewAccessibility")
    private fun showProgressBar() {
        Log.d("hatamAddDetailProgress"," show progressBar starat")
        val layoutParams= activity?.window?.attributes
        binding.layoutAddDetailProgress.visibility=View.VISIBLE
        activity?.window?.attributes = layoutParams
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )

        Log.d("hatamAddDetailProgress"," show progressBar finish")

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun hideProgressBar() {
        val layoutParams= activity?.window?.attributes
        Log.d("hatamAddDetailProgress"," hide progressBar starat")
        binding.layoutAddDetailProgress.visibility=View.GONE
        activity?.window?.attributes = layoutParams
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        Log.d("hatamAddDetailProgress"," hide progressBar finish")

    }



}