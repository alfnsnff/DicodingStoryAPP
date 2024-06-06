package com.dicoding.storyapp.view.story.detail

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.storyapp.databinding.ActivityDetailStoryBinding
import com.dicoding.storyapp.view.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Locale

class DetailStoryActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val storyId = intent.getStringExtra(STORY_ID)
        if (storyId != null) {
            observeStories()
            viewModel.getDetailStory(storyId)
        } else {
            Log.e("DetailStoryActivity", "Story ID is null")
            // Handle the error appropriately (e.g., show a message to the user)
        }
    }

    private fun observeStories() {
        viewModel.detailStory.observe(this) { detailStoryResponse ->
            detailStoryResponse?.story?.let { story ->
                Log.d("DetailStoryActivity", "Received story response: $story")
                Glide.with(binding.ivItemPhoto.context)
                    .load(story.photoUrl)
                    .into(binding.ivItemPhoto)
                binding.tvItemName.text = story.name
                binding.tvItemDesc.text = story.description
                val formattedDateTime = story.createdAt?.let { formatDate(it) }
                binding.tvItemTime.text = formattedDateTime
            }
        }
    }

    private fun formatDate(dateTimeString: String): String? {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val parsedDate = dateFormat.parse(dateTimeString)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return parsedDate?.let { outputFormat.format(it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        const val STORY_ID = "story_id"
    }
}
