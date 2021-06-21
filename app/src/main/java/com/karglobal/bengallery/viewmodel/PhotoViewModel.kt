package com.karglobal.bengallery.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.karglobal.bengallery.common.Param.Companion.MAX_PAGE_NUM
import com.karglobal.bengallery.common.Param.Companion.PAGE_NUM
import com.karglobal.bengallery.model.PhotoModel
import com.karglobal.bengallery.repository.RemoteRepository
import kotlinx.coroutines.launch

class PhotoViewModel(private val repository: RemoteRepository) : ViewModel() {

    private val isLoading = MutableLiveData<Boolean>()
    private var errorMessage = MutableLiveData<String>()
    private val photos = MutableLiveData<List<PhotoModel>>()


    fun isLoading(): LiveData<Boolean> = isLoading
    fun errorMessage(): LiveData<String> = errorMessage
    fun getPhotos(): LiveData<List<PhotoModel>> = photos

    fun downloadPhotos() {
        isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val result = repository.getPhotos()
                photos.postValue(result)
                if (PAGE_NUM <= MAX_PAGE_NUM) {
                    PAGE_NUM += 1
                    downloadPhotos()
                }
                isLoading.postValue(false)
            } catch (e: Exception) {
                isLoading.postValue(false)
                errorMessage.postValue(e.localizedMessage)
            }
        }
    }
}