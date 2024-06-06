package com.dicoding.storyapp.view.story.addStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.api.response.FileUploadResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddStoryViewModel(private val repository: UserRepository) : ViewModel() {

    private val _uploadResult = MutableLiveData<FileUploadResponse>()
    val uploadResult: LiveData<FileUploadResponse> = _uploadResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun uploadImage(multipartBody: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            try {
                val user = repository.getSession().first()
                val response = repository.uploadImage(user.token, multipartBody, description)
                _uploadResult.postValue(response)
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                _errorMessage.postValue(errorBody ?: "Unknown error occurred")
            } catch (e: Exception) {
                _errorMessage.postValue(e.message ?: "Unknown error occurred")
            }
        }
    }
}


