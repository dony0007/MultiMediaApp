package com.example.music

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.music.model.VideoListActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btnmusic = findViewById<Button>(R.id.bt_music)
        var btnvideo = findViewById<Button>(R.id.bt_video)


        btnmusic.setOnClickListener {
            var i = Intent(applicationContext,MusicActivity::class.java)
            startActivity(i)
        }

        btnvideo.setOnClickListener {
            var i = Intent(applicationContext,VideoListActivity::class.java)
            startActivity(i)
        }

    }
}