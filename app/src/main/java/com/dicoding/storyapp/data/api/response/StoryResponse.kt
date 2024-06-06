package com.dicoding.storyapp.data.api.response

data class StoryResponse(
	val listStory: List<ListStoryItem> = emptyList(),
	val error: Boolean? = null,
	val message: String? = null
)

data class ListStoryItem(
	val photoUrl: String? = null,
	val createdAt: String? = null,
	val name: String? = null,
	val description: String? = null,
	val lon: Double? = null,
	val id: String? = null,
	val lat: Double? = null
)

