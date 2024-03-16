package com.example.mevltbul.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mevltbul.Classes.User
import com.example.mevltbul.Repository.UserDaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

@HiltViewModel
class UserVM @Inject constructor(var provideUserDaoRepo: UserDaoRepository) : ViewModel() {
    private val  _userData= MutableStateFlow<User>(User())
    val userData:StateFlow<User> = _userData
    fun getUserData(){
        viewModelScope.launch {
            provideUserDaoRepo.getUserData().collect {
                _userData.value=it
            }
        }
    }
    fun createUser(userName:String,email:String,password:String,isCreate:(Boolean) -> Unit){
        provideUserDaoRepo.createUser(userName,email,password,isCreate)
    }
    fun loginUser(email:String,password:String, isLogin:(Boolean) -> Unit){
        provideUserDaoRepo.loginUser(email,password, isLogin)
    }
    fun logoutUser(){
        provideUserDaoRepo.logoutUser()
    }

    fun changePassword(newPassword:String,isChange:(Boolean) -> Unit){
        provideUserDaoRepo.changePassword(newPassword){
            isChange(it)
        }
    }

    fun changeEmail(newEmail:String,isChange:(Boolean) -> Unit){
        provideUserDaoRepo.changeEmail(newEmail){
            isChange(it)
        }
    }

    fun checkUserPassword(password:String,isCheck:(Boolean) -> Unit){
        provideUserDaoRepo.checkUserPassword(password){
            isCheck(it)
        }
    }

//    fun getUserData2():Flow<User> = channelFlow {
//      val job=  viewModelScope.launch {
//
//            try {
//                provideUserDaoRepo.getUserData().collect {
//                    send(it)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }finally {
//                close()
//            }
//
//
//        }
//        job.cancel()
//
//    }
//
}