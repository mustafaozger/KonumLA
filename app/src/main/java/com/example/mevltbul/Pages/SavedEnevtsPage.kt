package com.example.mevltbul.Pages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mevltbul.Adapter.MainPageExploreRcylerAdapter
import com.example.mevltbul.ViewModel.DetailVM
import com.example.mevltbul.ViewModel.UserVM
import com.example.mevltbul.databinding.FragmentSavedEnevtsPageBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class SavedEnevtsPage : Fragment() {

    private lateinit var binding:FragmentSavedEnevtsPageBinding
    private lateinit var userVM: UserVM
    private lateinit var detailVM: DetailVM


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val tempUserVM:UserVM by viewModels()
        userVM=tempUserVM
        val tempDetailVM:DetailVM by viewModels()
        detailVM=tempDetailVM
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentSavedEnevtsPageBinding.inflate(inflater,container,false)

        viewLifecycleOwner.lifecycleScope.launch {
            userVM.userData.collect(){user->
                if (user.saved_location_list!=null){
                    detailVM.getMessageRooms(user.saved_location_list!!)
                    detailVM.savedRoomLiveData.observe(viewLifecycleOwner){
                        if (it!=null){
                            val adapter= MainPageExploreRcylerAdapter(requireContext(),it)
                            binding.savedPageRcyler.adapter=adapter
                            binding.savedPageRcyler.layoutManager=
                                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        }

                    }

                }
            }

        }

        return binding.root
    }

}