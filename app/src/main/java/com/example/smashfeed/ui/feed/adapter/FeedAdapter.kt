package com.example.smashfeed.ui.feed.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smashfeed.R
import com.example.smashfeed.data.model.Post
import com.example.smashfeed.data.model.PostWithUser
import com.google.android.material.imageview.ShapeableImageView
import java.io.File


class FeedAdapter(
    var posts: List<PostWithUser>,
    private val onLikeClick: (Post) -> Unit,
    private val onSaveClick: (Post) -> Unit
): RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    private val expandedIds = mutableSetOf<Int>()

    class FeedViewHolder(row: View): RecyclerView.ViewHolder(row) {
        val ivAvatar = row.findViewById<ShapeableImageView>(R.id.ivAvatar)
        val tvUsername = row.findViewById<TextView>(R.id.tvUsername)
        val ivPostImage = row.findViewById<ImageView>(R.id.ivPostImage)
        val btnLike = row.findViewById<ImageButton>(R.id.btnLike)
        val tvLikes = row.findViewById<TextView>(R.id.tvLikes)
        val btnSave = row.findViewById<ImageButton>(R.id.btnSave)
        val tvDescription = row.findViewById<TextView>(R.id.tvDescription)
        val tvReadMore = row.findViewById<TextView>(R.id.tvReadMore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val view = android.view.LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feed, parent, false)
        return FeedViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val postWithUser = posts[position]
        val post = postWithUser.post
        val user = postWithUser.user

        holder.tvUsername.text = user.username

        val avatarFile = File(user.avatar)
        if (avatarFile.exists()) {
            holder.ivAvatar.setImageURI(Uri.fromFile(avatarFile))
        } else {
            holder.ivAvatar.setImageResource(R.drawable.ic_sports_padel_24px)
        }

        holder.tvLikes.text = post.likes.toString()
        holder.btnLike.isActivated = post.isLiked
        holder.btnSave.isActivated = post.saved
        holder.btnLike.setOnClickListener { onLikeClick(post) }
        holder.btnSave.setOnClickListener { onSaveClick(post) }

        val file = File(post.path)
        if (file.exists()) {
            holder.ivPostImage.setImageURI(Uri.fromFile(file))
        }

        val postId = post.id ?: position
        val isExpanded = expandedIds.contains(postId)
        val toggle = {
            if (expandedIds.contains(postId)) expandedIds.remove(postId) else expandedIds.add(postId)
            notifyItemChanged(holder.bindingAdapterPosition)
        }

        holder.tvDescription.text = post.description
        holder.tvDescription.maxLines = if (isExpanded) Int.MAX_VALUE else 1
        holder.tvDescription.ellipsize = if (isExpanded) null else android.text.TextUtils.TruncateAt.END

        if (isExpanded) {
            holder.tvReadMore.visibility = View.VISIBLE
            holder.tvReadMore.text = "Ver menos"
            holder.tvDescription.setOnClickListener { toggle() }
            holder.tvReadMore.setOnClickListener { toggle() }
        } else {
            holder.tvReadMore.visibility = View.GONE
            holder.tvDescription.setOnClickListener(null)
            holder.tvReadMore.setOnClickListener(null)
            holder.tvDescription.tag = postId
            holder.tvDescription.post {
                if (holder.tvDescription.tag != postId) return@post
                val layout = holder.tvDescription.layout ?: return@post
                val isTruncated = layout.getEllipsisCount(layout.lineCount - 1) > 0
                if (isTruncated) {
                    holder.tvReadMore.visibility = View.VISIBLE
                    holder.tvReadMore.text = "Ver más"
                    holder.tvDescription.setOnClickListener { toggle() }
                    holder.tvReadMore.setOnClickListener { toggle() }
                }
            }
        }
    }

    override fun getItemCount(): Int = posts.size

    @SuppressLint("NotifyDataSetChanged")
    fun updatePosts(newPosts: List<PostWithUser>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}