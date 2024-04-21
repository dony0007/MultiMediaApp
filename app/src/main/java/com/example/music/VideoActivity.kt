package com.example.music


import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView

class VideoActivity : AppCompatActivity() {
    lateinit var offlineUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)

        var videoView = findViewById<VideoView>(R.id.videoView)
        var playPause = findViewById<ImageView>(R.id.imageView8)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)

        var uri = intent.getStringExtra("uri")
        var title = intent.getStringExtra("title")
        if (uri != null){
            offlineUri = Uri.parse(uri)
            Toast.makeText(applicationContext,"Uri"+title,Toast.LENGTH_SHORT).show()
        }else {
            Toast.makeText(applicationContext,"Not Uri",Toast.LENGTH_SHORT).show()
            offlineUri = Uri.parse("android.resource://$packageName/${R.raw.aavesham}")
        }

        videoView.setMediaController(mediaController)
        videoView.setVideoURI(offlineUri)
        videoView.requestFocus()
        videoView.start()

        playPause.setOnClickListener {
            if (!videoView.isPlaying) {
                videoView.start()
                playPause.setImageResource(R.drawable.pause)
            } else {
                videoView.pause()
                playPause.setImageResource(R.drawable.play)
            }
        }

    }
}









