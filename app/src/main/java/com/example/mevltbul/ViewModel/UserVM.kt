package com.example.mevltbul.ViewModel

import androidx.lifecycle.ViewModel
import com.example.mevltbul.Classes.User
import com.example.mevltbul.Repository.UserDaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
@HiltViewModel
class UserVM @Inject constructor(var provideUserDaoRepo: UserDaoRepository) : ViewModel() {

    fun createUser(userName:String,email:String,password:String,isCreate:(Boolean) -> Unit){
        provideUserDaoRepo.createUser(userName,email,password,isCreate)
    }
    fun loginUser(email:String,password:String, isLogin:(Boolean) -> Unit){
        provideUserDaoRepo.loginUser(email,password, isLogin)
    }
    fun logoutUser(){
        provideUserDaoRepo.logoutUser()
    }
    fun getUserData():Flow<User> = flow {
        provideUserDaoRepo.getUserData()
    }
}