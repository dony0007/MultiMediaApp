package com.example.music.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.music.R
import com.example.music.VideoActivity
import com.example.music.model.VideoItemModel

class VideoAdapter(val context: Context,private val data: List<VideoItemModel>): RecyclerView.Adapter<VideoAdapter.ViewHolder>()  {

    private lateinit var parent: ViewGroup

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val thumbnail: ImageView = itemView.findViewById(R.id.videoThumbnail)
        val title: TextView = itemView.findViewById(R.id.vTitle)
        val duration: TextView = itemView.findViewById(R.id.vDuration)
        val image: ImageView = itemView.findViewById(R.id.videoThumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.parent = parent
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.video_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]

        holder.title.text = item.title
        holder.duration.text = item.duration.toString()
        Glide.with(context)
            .asBitmap()
            .load(item.artUri)
            .into(holder.image)
        holder.itemView.setOnClickListener {
            var i = Intent(holder.itemView.context, VideoActivity::class.java)
            i.putExtra("uri",item.artUri.toString())
            i.putExtra("title",item.title)
            holder.itemView.context.startActivity(i)
        }
    }
}