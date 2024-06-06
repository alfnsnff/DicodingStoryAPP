package com.dicoding.storyapp.view.main

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.storyapp.data.api.response.ListStoryItem
import com.dicoding.storyapp.databinding.ItemStoryBinding
import com.dicoding.storyapp.view.story.detail.DetailStoryActivity
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.core.util.Pair

class MainAdapter : ListAdapter<ListStoryItem, MainAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val view = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val story = getItem(position) // Accessing the item from the provided list
        holder.bind(story)
        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailStoryActivity::class.java)
            intentDetail.putExtra(DetailStoryActivity.STORY_ID, story.id)

            // Define shared elements
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.ivItemPhoto, "photo"), // Shared image view
                    Pair(holder.binding.tvItemName, "name"),       // Shared text view for name
                    Pair(holder.binding.tvItemDesc, "desc") // Shared text view for description
                )

            // Start activity with shared element transition
            holder.itemView.context.startActivity(intentDetail, optionsCompat.toBundle())
        }
    }


    inner class StoryViewHolder(val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: ListStoryItem) {
            Glide.with(binding.ivItemPhoto.context)
                .load(story.photoUrl)
                .into(binding.ivItemPhoto)
            binding.tvItemName.text = story.name
            binding.tvItemDesc.text = story.description
            val formattedDateTime = story.createdAt?.let { formatDate(it) }
            binding.tvItemTime.text = formattedDateTime
        }
    }

    private fun formatDate(dateTimeString: String): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val parsedDate = dateFormat.parse(dateTimeString)
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return outputFormat.format(parsedDate)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id // Assuming id is a unique identifier for a story
            }

            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem == newItem
            }
        }

    }
}
