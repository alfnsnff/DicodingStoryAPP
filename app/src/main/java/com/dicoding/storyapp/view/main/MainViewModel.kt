package com.dicoding.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.api.response.StoryResponse
import com.dicoding.storyapp.data.pref.UserModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _storiesLiveData = MutableLiveData<StoryResponse>()
    val storiesLiveData: LiveData<StoryResponse>
        get() = _storiesLiveData

    fun getStories() {
        viewModelScope.launch {
            try {
                val user = repository.getSession()
                    .first() // Assuming you can get the user with the token from session
                val stories = repository.getStories(user.token)
                _storiesLiveData.postValue(stories)
            } catch (e: Exception) {
                // Handle exceptions here
                // Log the error or show an error message
            }
        }
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}