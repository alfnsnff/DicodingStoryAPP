package com.dicoding.storyapp.view.story.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.api.response.DetailStoryResponse
import com.dicoding.storyapp.data.api.response.StoryResponse
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DetailStoryViewModel(private val repository: UserRepository) : ViewModel() {


    private val _detailStory = MutableLiveData<DetailStoryResponse>()
    val detailStory: LiveData<DetailStoryResponse>
        get() = _detailStory

    fun getDetailStory(storyId: String) {
        viewModelScope.launch {
            try {
                val user = repository.getSession()
                    .first()
                val detailStory = repository.getDetailStory(user.token, storyId)
                _detailStory.postValue(detailStory)
            } catch (e: Exception) {
                // Handle exceptions here
                // Log the error or show an error message
            }
        }
    }

}
