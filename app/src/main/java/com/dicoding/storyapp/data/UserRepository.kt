package com.dicoding.storyapp.data

import com.dicoding.storyapp.data.api.response.DetailStoryResponse
import com.dicoding.storyapp.data.api.response.FileUploadResponse
import com.dicoding.storyapp.data.api.response.StoryResponse
import com.dicoding.storyapp.data.api.retrofit.ApiConfig
import com.dicoding.storyapp.data.api.retrofit.ApiService
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    suspend fun getStories(token: String): StoryResponse {
        return try {
            val apiService = ApiConfig.getApiService(token)
            apiService.getStories()
        } catch (e: Exception) {
            StoryResponse()
        }
    }

    suspend fun getDetailStory(token: String, id: String): DetailStoryResponse {
        return try {
            val apiService = ApiConfig.getApiService(token)
            apiService.getDetailStory(id)
        } catch (e: Exception) {
            DetailStoryResponse()
        }
    }

    suspend fun uploadImage(
        token: String,
        photo: MultipartBody.Part,
        description: RequestBody
    ): FileUploadResponse {
        return try {
            val apiService = ApiConfig.getApiService(token)
            apiService.uploadImage(photo, description)
        } catch (e: Exception) {
            FileUploadResponse(error = true, message = e.message ?: "Unknown error")
        }
    }


    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null

        fun getInstance(apiService: ApiService, userPreference: UserPreference): UserRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = UserRepository(apiService, userPreference)
                INSTANCE = instance
                instance
            }
        }
    }
}