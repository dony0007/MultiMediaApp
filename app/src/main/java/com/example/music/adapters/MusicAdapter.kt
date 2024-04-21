package com.example.music.adapters

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.music.R
import com.example.music.model.MusicListItem
import com.example.music.model.formatDuration

class MusicAdapter(
    val context: Context,
    private val data: List<MusicListItem>,
    public val mediaPlayer: MediaPlayer,
    val playPause: ImageView,
    val musicCover: ImageView
): RecyclerView.Adapter<MusicAdapter.ViewHolder>()   {
    private lateinit var parent: ViewGroup

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val musicCover: ImageView = itemView.findViewById(R.id.musicCover)
        val mTitle: TextView = itemView.findViewById(R.id.musicTitle)
        val mDuration: TextView = itemView.findViewById(R.id.musicDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.parent = parent
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.music_item, parent, false)
        return MusicAdapter.ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.mTitle.text = item.title
        holder.mDuration.text = formatDuration(item.duration)
//
        Glide.with(context)
            .load(item.artUri)
            .apply(RequestOptions().placeholder(R.drawable.image1).centerCrop())
            .into(holder.musicCover)
//
        holder.itemView.setOnClickListener {
            musicCover.setImageResource(R.drawable.image1)
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(item.path)
            mediaPlayer!!.prepare()
            if (!mediaPlayer.isPlaying) {
                mediaPlayer.start()
                playPause.setImageResource(R.drawable.pause)
            } else {
                mediaPlayer.pause()
                playPause.setImageResource(R.drawable.play)
            }
        }
    }
}